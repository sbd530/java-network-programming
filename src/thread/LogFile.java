package thread;

import java.io.*;
import java.util.Date;

public class LogFile {

    private final Writer out;

    public LogFile(File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        this.out = new BufferedWriter(fw);
    }

    /**
     * 로그파일에 쓰기를 실행할 때 다른 스레드가 끼어들지 못하도록 Writer 객체를 동기화시킨다.
     */
    public void writeEntry(String message) throws IOException {
        synchronized (out) {
            Date d = new Date();
            out.write(d.toString());
            out.write('\t');
            out.write(message);
            out.write("\r\n");
        }
    }

    public void close() throws IOException {
        out.flush();
        out.close();
    }
}
