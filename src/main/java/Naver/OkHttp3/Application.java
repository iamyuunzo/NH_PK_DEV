package Naver.OkHttp3;

public class Application {

    public static void main(String[] args) {
        try {
            // 실제 발급받은 값 넣기
            String clientId = "네이버_클라이언트_ID";
            String clientSecret = "네이버_클라이언트_시크릿";

            // GetToken 객체 생성
            GetToken getToken = new GetToken();

            // 토큰 발급 메서드 호출
            String response = getToken.getToken(clientId, clientSecret);

            // 결과 출력
            System.out.println("토큰 응답: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}