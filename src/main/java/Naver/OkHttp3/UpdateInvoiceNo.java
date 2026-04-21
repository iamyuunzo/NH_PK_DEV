package Naver.OkHttp3;

import okhttp3.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 네이버 발송 처리 API 호출
 * @param token         네이버 access token
 * @param orderNo       상품주문번호
 * @param invoiceNo     송장번호
 * @param deliveryDate  배송일자(yyyyMMdd)
 * @return 상품 조회 API 응답 body 문자열
 * @throws Exception 요청 실패 또는 응답 처리 중 예외 발생 시 전달
 * DB 저장, 가공 등의 로직 X, API 조회만 있는 테스트용 코드
 */
public class UpdateInvoiceNo {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String updateInvoiceNo(String token) throws Exception {

//        // 0. 배송일 (단일 날짜/시간 생성)
//        String dispatchDate = LocalDate.parse(delivery_date, DateTimeFormatter.ofPattern("yyyyMMdd"))
//                .atTime(LocalDateTime.now().toLocalTime())
//                .atZone(ZoneId.of("Asia/Seoul"))
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        // API Docs 그대로 작성한 버전 (안에 값 수정 가능)
        String json = """
                {
                  "dispatchProductOrders": [
                    {
                      "productOrderId": "실제_상품_주문번호",
                      "deliveryMethod": "DELIVERY(택배/등기/소포)",
                      "deliveryCompanyCode": "CJGLS(CJ대한통운)",
                      "trackingNumber": "실제_송장_번호",
                      "dispatchDate": "실제_배송일"
                    }
                  ]
                }
                """;

        // 1. Request : application/json 구성
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json")
        );

        // 2. Request : 요청 생성
        Request request = new Request.Builder()
                .url("https://api.commerce.naver.com/v1/pay-order/seller/product-orders/dispatch")
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
                throw new RuntimeException("네이버 발송 처리 실패: " + response.code() + " / " + responseBody);
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
    RequestBody body = RequestBody.create(mediaType, "{\n  \"dispatchProductOrders\": [\n    {\n      \"productOrderId\": \"2022040521691281\",\n      \"deliveryMethod\": \"DELIVERY\",\n      \"deliveryCompanyCode\": \"string\",\n      \"trackingNumber\": \"\",\n      \"dispatchDate\": \"2022-04-05T12:17:35.000+09:00\"\n    }\n  ]\n}");
    Request request = new Request.Builder()
      .url("https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/dispatch")
      .method("POST", body)
      .addHeader("Content-Type", "application/json")
      .addHeader("Accept", "application/json")
      .addHeader("Authorization", "Bearer <token>")
      .build();
    Response response = client.newCall(request).execute();
 */