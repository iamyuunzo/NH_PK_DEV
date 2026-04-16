package Naver.HttpUrlConnection;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// https://apicenter.commerce.naver.com/docs/commerce-api/current/exchange-sellers-auth
// 2. 인증 토큰 발급
public class getAccessToken {

    public static void main(String[] args) throws Exception {

        // API 요청 URL
        String url = "https://api.commerce.naver.com/external/v1/oauth2/token";

        // client_id (애플리케이션 ID)
        String clientId = "네이버_클라이언트_ID";

        // client_secret (애플리케이션 Secret)
        String clientSecret = "네이버_클라이언트_시크릿";

        // 타임스탬프 (전자서명 생성 시 사용된 밀리초 단위의 Unix 시간)
        long timestamp = System.currentTimeMillis();

        String sign = SignatureGenerator.generateSignature(clientId, clientSecret, timestamp);

        // Request Body 파라미터
        String params = "grant_type=client_credentials"
                + "&client_id=" + clientId
                + "&timestamp=" + timestamp
                + "&client_secret_sign=" + sign
                + "&type=SELF";

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        // Method
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   // Request
        conn.setRequestProperty("Accept", "application/json");  // Response
        conn.setDoOutput(true);

        // Body 전송
        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        System.out.println("Response status: " + responseCode);

        // Response body 읽기
        BufferedReader br;

        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line;
        StringBuilder response = new StringBuilder();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        br.close();

        // 전체 JSON 출력
        System.out.println("Response : " + response.toString());
    }
}