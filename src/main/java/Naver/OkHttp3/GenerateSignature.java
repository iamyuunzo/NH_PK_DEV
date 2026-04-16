package Naver.OkHttp3;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 네이버 커머스 API용 전자서명 생성 메서드
 * @param clientId     애플리케이션 Client ID
 * @param clientSecret 애플리케이션 Client Secret
 * @param timestamp    밀리초 단위 timestamp
 * @return Base64 인코딩된 bcrypt 결과 문자열
 */
public class GenerateSignature {

    public static String generateSignature(String clientId, String clientSecret, long timestamp) {

        // 네이버 API docs용 문법
        String password = clientId + "_" + timestamp;

        // Bcrypt 해싱
        String hashedPw = BCrypt.hashpw(password, clientSecret);

        // Base64 URL-safe 인코딩 후 반환
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }
}


/*
* Naver Commerce Example
* https://apicenter.commerce.naver.com/docs/auth#%EC%A0%84%EC%9E%90%EC%84%9C%EB%AA%85

  class SignatureGenerator {
    public static String generateSignature(String clientId, String clientSecret, Long timestamp) {

        // 밑줄로 연결하여 password 생성
        String password = StringUtils.joinWith("_", clientId, timestamp);

        // bcrypt 해싱
        String hashedPw = BCrypt.hashpw(password, clientSecret);

        // base64 인코딩
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String args[]) {
        String clientId = "aaaabbbbcccc";
        String clientSecret = "$2a$10$abcdefghijklmnopqrstuv";
        Long timestamp = System.currentTimeMillis(); // 1643961623299L로 가정
        System.out.println(generateSignature(clientId, clientSecret, timestamp));
    }
}

  // 실행 결과 예시
  JDJhJDEwJGFiY2RlZmdoaWprbG1ub3BxcnN0dXVCVldZSk42T0VPdEx1OFY0cDQxa2IuTnpVaUEzbmsy
 */


