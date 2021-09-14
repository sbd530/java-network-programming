package urlConnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

/**
 * URL 이 마지막으로 수정된 시간 구하기
 * HEAD 메서드 이용
 */
public class LastModified {

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                URL u = new URL(args[i]);
                HttpURLConnection http = (HttpURLConnection) u.openConnection();
                http.setRequestMethod("HEAD");
                System.out.println(u + " was last modified at " +
                        new Date(http.getLastModified()));
            } catch (MalformedURLException e) {
                System.err.println(args[i] + " is not a URL I understand");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }
}
