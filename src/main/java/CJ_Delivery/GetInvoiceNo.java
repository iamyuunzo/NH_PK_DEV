package CJ_Delivery;

import okhttp3.*;

/**
 * CJ대한통운 DX API용 운송장 번호 생성 요청
 * - 인증 방식: CJ-Gateway-APIKey 헤더 사용
 * - 요청 body: DATA 객체 내부에 고객 정보와 1Day 토큰 전달
 * - EndPoint : /ReqInvcNo
 * @param token 1Day 토큰 발급 API에서 받은 TOKEN_NUM
 * @return API 응답 본문(String)
 * @throws Exception 운송장 번호 생성 실패 또는 통신 오류 발생 시 예외 전달
 */
public class GetInvoiceNo {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String getInvoiceNo(String token) throws Exception {

        // API Docs 그대로 작성한 버전
        String json = """
                {
                  "DATA": {
                    "CLNTNUM": "실제_고객ID",
                    "TOKEN_NUM": "실제_발급받은_토큰(%s)",
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
                // .url("https://dxapi.cjlogistics.com:5052/gateway/PA-P-ReqInvcNo/1.0/ReqInvcNo")
                // Gateway 경로 없이 Direct로 호출하는 방식 (개발 URL)
                .url("https://dxapi.cjlogistics.com:5054/ReqInvcNo")
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
                throw new RuntimeException("운송장 번호 발급 실패: " + response.code() + " / " + responseBody);
            }

            return responseBody;
        }
    }
}
