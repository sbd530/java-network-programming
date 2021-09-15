package thread;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;

public class GZipAllFiles{

    public final static int THREAD_COUNT = 4;

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

        for (String filename : args) {
            File f = new File(filename);
            if (f.exists()) {
                if (f.isDirectory()) {
                    File[] files = f.listFiles();
                    for (File file : files) {
                        // 디렉터리 하위의 디렉터리는 처리하지 않는다.
                        if (!file.isDirectory()) {
                            Runnable task = new GZipRunnable(file);
                            pool.submit(task);
                        }
                    }
                } else {
                    Runnable task = new GZipRunnable(f);
                    pool.submit(task);
                }
            }
        }
        pool.shutdown();
    }

    static class GZipRunnable implements Runnable {
        private final File input;

        public GZipRunnable(File input) {
            this.input = input;
        }

        @Override
        public void run() {
            // 압축 파일은 다시 압축하지 않는다.
            if (!input.getName().endsWith(".gz")) {
                File output = new File(input.getParent(), input.getName() + ".gz");

                // 이미 존재하는 파일을 덮어쓰지 않는다.
                if (!output.exists()) {
                    try (
                            InputStream in = new BufferedInputStream(new FileInputStream(input));
                            OutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(output)));
                    ) {
                        int b;
                        while ((b = in.read()) != -1) out.write(b);
                        out.flush();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }


}
