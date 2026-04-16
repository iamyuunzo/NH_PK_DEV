package Naver.HttpUrlConnection;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

// https://apicenter.commerce.naver.com/docs/auth
// 1. 전자서명 발급 test
public class SignatureGenerator {

    // 전자서명 생성 방법
    public static String generateSignature(String clientId, String clientSecret, Long timestamp) {

        // 1. password 생성 : 밑줄로 연결하여 password 생성 (clientId_timestamp)
        String password = clientId + "_" + timestamp;

        // 2. bcrypt 해싱
        String hashedPw = BCrypt.hashpw(password, clientSecret);

        // 3. base64 인코딩 (전자서명 생성되는 값)
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }

    /*
    * client_id: 애플리케이션 ID
    * client_secret: 애플리케이션 시크릿
    * timestamp: 밀리초(millisecond) 단위의 Unix 시간
    */
    public static void main(String[] args) {

        // client_id
        String clientId = "네이버_클라이언트_ID";

        // client_secret
        String clientSecret = "네이버_클라이언트_시크릿";

        // timestamp
        Long timestamp = System.currentTimeMillis();

        String sign = generateSignature(clientId, clientSecret, timestamp);

        System.out.println("timestamp = " + timestamp);
        System.out.println("client_secret_sign = " + sign);
    }
}