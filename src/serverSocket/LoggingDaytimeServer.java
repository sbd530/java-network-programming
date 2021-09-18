package serverSocket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingDaytimeServer {
    public final static int PORT = 13;
    private final static Logger auditLogger = Logger.getLogger("requests");
    private final static Logger errorLogger = Logger.getLogger("errors");

    public static void main(String[] args) throws IOException {

        ExecutorService pool = Executors.newFixedThreadPool(50);

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket connection = server.accept();
                    Callable<Void> task = new DaytimeTask(connection);
                    pool.submit(task);
                } catch (IOException e) {
                    errorLogger.log(Level.SEVERE, "accept error", e);
                } catch (RuntimeException e) {
                    errorLogger.log(Level.SEVERE, "unexpected error" + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            errorLogger.log(Level.SEVERE, "couldn't start server", e);
        } catch (RuntimeException e) {
            errorLogger.log(Level.SEVERE, "couldn't start server" + e.getMessage(), e);
        }
    }

    private static class DaytimeTask implements Callable<Void> {
        private Socket connection;

        DaytimeTask(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() {
            try {
                Date now = new Date();
                // 클라이언트가 연결을 종료하면 먼저 로그를 남긴다.
                auditLogger.info(now + " " + connection.getRemoteSocketAddress());
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                out.write(now.toString() + "\r\n");
                out.flush();
            } catch (IOException e) {

            } finally {
                try {
                    connection.close();
                } catch (IOException e) {

                }
            }
            return null;
        }
    }
}
