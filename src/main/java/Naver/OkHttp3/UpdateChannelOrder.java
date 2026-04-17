package Naver.OkHttp3;

import okhttp3.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 네이버 조건형 상품 주문 조회 API 호출
 * @param token 토큰 발급 API에서 받은 access token
 * @return 주문 조회 API 응답 body 문자열
 * @throws Exception 요청 실패 또는 응답 처리 중 예외 발생 시 전달
 * DB 저장, 가공 등의 로직 X, API 조회만 있는 테스트용 코드
 */
public class UpdateChannelOrder {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String updateChannelOrder(String token) throws Exception {

        // 0. RESTful API : ISO 8601 규격 날짜 및 시간 형식
        // https://apicenter.commerce.naver.com/docs/restful-api
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        ZonedDateTime fromTime = now.minusDays(1)
                .withHour(15)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        ZonedDateTime toTime = now
                .withHour(14)
                .withMinute(59)
                .withSecond(59)
                .withNano(0);

        // ISO 8601 문자열 변환
        String from = fromTime.format(formatter); // 시작 시간
        String to = toTime.format(formatter); // 종료 시간

        // 1. Request : text/plain 구성
        // GET 요청이므로 body 대신 query parameter를 URL에 추가
        HttpUrl url = HttpUrl.parse("https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders")
                .newBuilder()
                // 예시 파라미터
                // 실제 문서 조건에 맞는 파라미터명/값으로 바꿔야 함
                .addQueryParameter("from", from)
                .addQueryParameter("to", to)
                .addQueryParameter("rangeType", "PAYED_DATETIME")
                .addQueryParameter("productOrderStatus", "PAYED")
                .addQueryParameter("quantityClaimCompatibility", "true")
                .build();

        // 2. Request : 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
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
                throw new RuntimeException("주문 조회 실패: " + response.code() + " / " + responseBody);
            }

            return responseBody;
        }
    }
}


/*
* Naver Commerce Example
  OkHttpClient client = new OkHttpClient().newBuilder()
      .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
      .url("https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders")
      .method("GET", body)
      .addHeader("Accept", "application/json")
      .addHeader("Authorization", "Bearer <token>")
      .build();
    Response response = client.newCall(request).execute();
 */
