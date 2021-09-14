package urlConnection;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class FormPoster {
    private URL url;

    private QueryString query = new QueryString();

    public FormPoster(URL url) {
        if (!url.getProtocol().toLowerCase(Locale.ROOT).startsWith("http")) {
            throw new IllegalArgumentException("Posting only works for http URLs");
        }
        this.url = url;
    }

    public void add(String name, String value) {
        query.add(name, value);
    }

    public URL getURL() {
        return this.url;
    }

    public InputStream post() throws IOException {

        // 연결을 열고 POST로 전송하기 위한 준비
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);

        try (OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(), StandardCharsets.UTF_8)) {
            // POST 라인, Content-type 헤더, Content-length 헤더들은 URLConnection 에 의해 보내지므로 데이터만 보내면 된다.
            out.write(query.toString());
            out.write("\r\n");
            out.flush();
        }
        // 서버의 응답 반환
        return uc.getInputStream();
    }

    public static void main(String[] args) {
        URL url;
        if (args.length > 0) {
            try {
                url = new URL(args[0]);
            } catch (MalformedURLException e) {
                System.err.println("Usage: java FormPoster url");
                return;
            }
        } else {
            try {
                url = new URL("http://www.cafeaulait.org/books/jnp4/postquery.phtml");
            } catch (MalformedURLException e) { // 발생해서는 안되는 상황
                System.err.println(e);
                return;
            }
        }

        FormPoster poster = new FormPoster(url);
        poster.add("name", "Elliotte Rusty Harold");
        poster.add("email", "elharo@ibiblio.org");

        try (InputStream in = poster.post()) {
            // 응답 읽기
            Reader r = new InputStreamReader(in);
            int c;
            while ((c = r.read()) != -1) {
                System.out.print((char) c);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
