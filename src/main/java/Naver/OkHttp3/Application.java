package Naver.OkHttp3;

import org.json.JSONObject;

public class Application {

    public static void main(String[] args) {
        try {

            /* 1. GetToken.java */
            // 실제 발급받은 값 넣기
            String clientId = "네이버_클라이언트_ID";
            String clientSecret = "네이버_클라이언트_시크릿";

            // 1-1. GetToken 객체 생성
            GetToken getToken = new GetToken();

            // 1-2. 토큰 발급 메서드 호출
            String response = getToken.getToken(clientId, clientSecret);
            System.out.println("토큰 응답: " + response);

            // 1-3. 응답 JSON에서 access_token만 추출
            JSONObject jsonObject = new JSONObject(response);
            String accessToken = jsonObject.getString("access_token");

            /* 2. UpdateChannelItem.java */
            // 상품 조회 API 호출
            UpdateChannelItem updateChannelItem = new UpdateChannelItem();
            String itemResult = updateChannelItem.updateChannelItem(accessToken);
            System.out.println("상품 조회 응답: " + itemResult);

            /* 3. UpdateChannelItem.java */
            // 상품 조회 API 호출
            UpdateChannelOrder updateChannelOrder = new UpdateChannelOrder();
            String orderResult = updateChannelOrder.updateChannelOrder(accessToken);
            System.out.println("주문 조회 응답: " + orderResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}