package Naver.OkHttp3;

import okhttp3.*;
import static Naver.OkHttp3.GenerateSignature.generateSignature;

/**
 * 네이버 커머스 API용 인증 토큰 생성 (SELF 타입)
 * @param clientId 네이버 커머스 Client ID
 * @param clientSecret 네이버 커머스 Client Secret
 * @return access_token
 * @throws Exception 예외 발생 시 전달
 */
public class GetToken {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String getToken(String clientId, String clientSecret) throws Exception {

        // 1. timestamp 생성
        long timestamp = System.currentTimeMillis();

        // 2. 전자서명 생성
        String clientSecretSign = generateSignature(clientId, clientSecret, timestamp);

        // 3. Request : form-data 구성
        RequestBody body = new FormBody.Builder()
                .add("client_id", clientId)
                .add("timestamp", String.valueOf(timestamp))
                .add("grant_type", "client_credentials")
                .add("client_secret_sign", clientSecretSign)
                .add("type", "SELF")
                .build();

        // 4. Request : 요청 생성
        Request request = new Request.Builder()
                .url("https://api.commerce.naver.com/external/v1/oauth2/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();

        // 5. Response : 요청 실행

        // Legacy Code
        // Response response = client.newCall(request).execute();
        // String responseBody = response.body().string();
        // JSONObject json = new JSONObject(responseBody);

        // try-with-resources
        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new RuntimeException("응답 body가 비어 있습니다.");
            }

            String responseBody = response.body().string();

            System.out.println("응답 코드: " + response.code());
            System.out.println("응답 바디: " + responseBody);

            if (!response.isSuccessful()) {
                throw new RuntimeException("토큰 발급 실패: " + response.code() + " / " + responseBody);
            }

            return responseBody;
        }
    }
}


/*
* Naver Commerce Example
  OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
  MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
  RequestBody body = RequestBody.create(mediaType, "");
  Request request = new Request.Builder()
        .url("https://api.commerce.naver.com/external/v1/oauth2/token")
        .method("POST", body)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .addHeader("Accept", "application/json")
        .build();
  Response response = client.newCall(request).execute();
 */
