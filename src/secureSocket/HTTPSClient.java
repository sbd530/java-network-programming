package secureSocket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class HTTPSClient {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java HTTPSClient2 host");
            return;
        }

        int port = 443; // https 기본 포트
        String host = args[0];
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try(SSLSocket socket = (SSLSocket) factory.createSocket(host, port);) {

            // 모든 암호화 조합을 사용하도록 설정
            String[] supported = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(supported);

            Writer out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            // https 는 GET 요청 시 전체 URL 을 사용해야 한다.
            out.write("GET http://" + host + "/ HTTP/1.1\r\n");
            out.write("Host: " + host + "\r\n");
            out.write("\r\n");
            out.flush();

            // 응답 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 헤더 읽기
            String s;
            while (!(s = in.readLine()).equals("")) {
                System.out.println(s);
            }
            System.out.println();
            // 길이 읽기
            String contentLength = in.readLine();
            int length = Integer.MAX_VALUE;
            try {
                length = Integer.parseInt(contentLength.trim(), 16);
            } catch (NumberFormatException e) {
                // 서버가 응답 본문 첫줄에 content-length 를 보내지 않은 경우
            }
            System.out.println(contentLength);

            int c;
            int i = 0;
            while ((c = in.read()) != -1 && i++ < length) {
                System.out.write(c);
            }

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
