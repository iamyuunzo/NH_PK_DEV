package CJ_Delivery;

import okhttp3.*;

/**
 * CJ대한통운 DX API용 1Day 인증 토큰 발급 메서드
 * - 인증 방식: CJ-Gateway-APIKey 헤더 사용
 * - 요청 body: DATA 객체 내부에 고객사 정보 전달
 * - EndPoint : /ReqOneDayToken
 * @return API 응답 본문(String)
 * @throws Exception 토큰 발급 실패 또는 통신 오류 발생 시 예외 전달
 */
public class GetToken {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String getToken() throws Exception {

        // API Docs 그대로 작성한 버전
        String json = """
                {
                  "DATA": {
                    "CUST_ID": "실제_고객사_코드",
                    "BIZ_REG_NUM": "실제_사업자_번호",
                    "USER_ID" : "실제_중계업체_계정"
                  }
                }
                """;

        // 1. Request : application/json 구성
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json")
        );

        // 2. Request : 요청 생성
        Request request = new Request.Builder()
                // API Gateway 방식
                // .url("https://dxapi.cjlogistics.com:5052/gateway/PA-P-ReqOneDayToken/1.0/ReqOneDayToken")
                // Gateway 경로 없이 Direct로 호출하는 방식 (개발 URL)
                .url("https://dxapi.cjlogistics.com:5054/ReqOneDayToken")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("CJ-Gateway-APIKey", "실제_API_KEY")
                .build();

        // 3. Response : 요청 실행
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
