package Naver.OkHttp3;

import okhttp3.*;

/**
 * 네이버 상품 조회 API 호출
 * @param token 토큰 발급 API에서 받은 access token
 * @return 상품 조회 API 응답 body 문자열
 * @throws Exception 요청 실패 또는 응답 처리 중 예외 발생 시 전달
 * DB 저장, 가공 등의 로직 X, API 조회만 있는 테스트용 코드
 */
public class UpdateChannelItem {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String updateChannelItem(String token) throws Exception {

        // API Dosc 그대로 작성한 버전 (안에 값 수정 가능)
        String json = """
                {
                  "searchKeywordType": "CHANNEL_PRODUCT_NO",
                  "channelProductNos": [
                    0
                  ],
                  "originProductNos": [
                    0
                  ],
                  "groupProductNos": [
                    0
                  ],
                  "sellerManagementCode": "string",
                  "productStatusTypes": [
                    "WAIT"
                  ],
                  "page": 1,
                  "size": 50,
                  "orderType": "NO",
                  "periodType": "PROD_REG_DAY",
                  "fromDate": "2024-07-29",
                  "toDate": "2024-07-29"
                }
                """;

        // 1. Request : application/json 구성
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json")
        );

        // 2. Request : 요청 생성
        Request request = new Request.Builder()
                .url("https://api.commerce.naver.com/external/v1/products/search")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer " + token)
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
                throw new RuntimeException("상품 조회 실패: " + response.code() + " / " + responseBody);
            }

            return responseBody;
        }
    }
}


/*
* Naver Commerce Example
  OkHttpClient client = new OkHttpClient().newBuilder()
      .build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\n  \"searchKeywordType\": \"CHANNEL_PRODUCT_NO\",\n  \"channelProductNos\": [\n    0\n  ],\n  \"originProductNos\": [\n    0\n  ],\n  \"groupProductNos\": [\n    0\n  ],\n  \"sellerManagementCode\": \"string\",\n  \"productStatusTypes\": [\n    \"WAIT\"\n  ],\n  \"page\": 1,\n  \"size\": 50,\n  \"orderType\": \"NO\",\n  \"periodType\": \"PROD_REG_DAY\",\n  \"fromDate\": \"2024-07-29\",\n  \"toDate\": \"2024-07-29\"\n}");
    Request request = new Request.Builder()
      .url("https://api.commerce.naver.com/external/v1/products/search")
      .method("POST", body)
      .addHeader("Content-Type", "application/json")
      .addHeader("Accept", "application/json;charset=UTF-8")
      .addHeader("Authorization", "Bearer <token>")
      .build();
    Response response = client.newCall(request).execute();
 */