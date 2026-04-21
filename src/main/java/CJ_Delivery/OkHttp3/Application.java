package CJ_Delivery.OkHttp3;

// 1. 1Day 토큰 발급
// 2. 발급된 토큰으로 운송장 번호 생성
// 3. 발급된 토큰 + 운송장 번호로 예약 접수
// GetToken -> GetInvoiceNo -> RegBook
public class Application {

    public static void main(String[] args) {
        try {
            // 1. 토큰 발급 객체 생성
            GetToken getTokenApi = new GetToken();

            // 2. 실제 토큰 발급 호출
            String tokenResult = getTokenApi.getToken();
            System.out.println("토큰 발급 응답: " + tokenResult);
            String token = "실제_발급받은_TOKEN_NUM";

            // 3. 운송장 번호 생성 객체 생성
            GetInvoiceNo getInvoiceNoApi = new GetInvoiceNo();

            // 4. 인스턴스 메서드로 호출해야 함
            String invcResult = getInvoiceNoApi.getInvoiceNo(token);
            System.out.println("운송장 번호 생성 응답: " + invcResult);
            String invcNo = "실제_발급받은_운송장번호";

            // 5. 예약 접수 객체 생성
            RegBook regBookApi = new RegBook();

            // 6. 예약 접수 호출
            String orderResult = regBookApi.regBook(token, invcNo);
            System.out.println("예약 접수 응답: " + orderResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
