package CJ_Delivery.OkHttp3;

import okhttp3.*;

/**
 * CJ대한통운 DX API용 일반 예약 접수 요청
 * - 인증 방식: CJ-Gateway-APIKey 헤더 사용
 * - 요청 body: DATA 객체 내부에 예약 접수 정보와 1Day 토큰 전달
 * - EndPoint : /RegBook
 * @return API 응답 본문(String)
 * @throws Exception 예약 접수 실패 또는 통신 오류 발생 시 예외 전달
 */
public class RegBook {
    // 재사용 가능한 OkHttpClient
    private final OkHttpClient client = new OkHttpClient();

    public String regBook(String token, String invcNo) throws Exception {

        // API Docs 그대로 작성 (Eclipse에서 사용한 Field만)
        String json = """
                {
                  "DATA": {
                    "CUST_ID": "...",
                    "TOKEN_NUM": "...",
                    "RCPT_YMD": "20260420",
                    "CUST_USE_NO": "...",
                    "RCPT_DV": "01",
                    "WORK_DV_CD": "01",
                    "REQ_DV_CD": "01",
                    "MPCK_KEY": "...",
                    "CAL_DV_CD": "01",
                    "FRT_DV_CD": "01",
                    "CNTR_ITEM_CD": "01",
                    "BOX_TYPE_CD": "01",
                    "BOX_QTY": "1",
                    "CUST_MGMT_DLCM_CD": "...",
                    "SENDR_NM": "...",
                    "SENDR_TEL_NO1": "...",
                    "SENDR_TEL_NO2": "...",
                    "SENDR_TEL_NO3": "...",
                    "SENDR_ZIP_NO": "...",
                    "SENDR_ADDR": "...",
                    "SENDR_DETAIL_ADDR": "...",
                    "RCVR_NM": "...",
                    "RCVR_TEL_NO1": "010",
                    "RCVR_TEL_NO2": "1234",
                    "RCVR_TEL_NO3": "5678",
                    "RCVR_ZIP_NO": "...",
                    "RCVR_ADDR": "...",
                    "RCVR_DETAIL_ADDR": "...",
                    "INVC_NO": "...",
                    "PRT_ST": "01",
                    "DLV_DV": "01",
                    "REMARK_1": "[박스 파손시 환송(사고처리) 요망]",
                    "REMARK_2": "테스트입니다.",
                    "REMARK_3": "테스트입니다.",
                    "ARRAY": [
                      {
                        "MPCK_SEQ": "1",
                        "GDS_CD": "11",
                        "GDS_NM": "[6250.0] 테스트자료 ^^",
                        "GDS_QTY": "1",
                        "UNIT_CD": "11",
                        "UNIT_NM": "테스트1",
                        "GDS_AMT": "111"
                      },
                      ...
                    ]
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
                // .url("https://dxapi.cjlogistics.com:5052/gateway/PA-P-ReqOneDayToken/1.0/RegBook")
                // Gateway 경로 없이 Direct로 호출하는 방식 (개발 URL)
                .url("https://dxapi.cjlogistics.com:5054/RegBook")
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
