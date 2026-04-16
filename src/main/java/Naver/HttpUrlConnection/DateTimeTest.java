package Naver.HttpUrlConnection;

import java.time.ZonedDateTime;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/* API 응답은 ISO 8601 규격을 따름
   오프셋 : KST(UTC+09:00)
   https://apicenter.commerce.naver.com/docs/restful-api
*/
public class DateTimeTest {

    public static void main(String[] args) {

        // 파싱
        ZonedDateTime parsed = ZonedDateTime.parse("2023-07-25T10:10:10.100+09:00");
        System.out.println(parsed);

        // 포맷팅
        String formatted1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(ZonedDateTime.now());
        System.out.println(formatted1);

        // Instant.java 를 이용한 경우 - 포맷 준수에 대한 주의
        String formatted2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(Instant.now().atZone(ZoneOffset.of("+09:00")));
        System.out.println(formatted2);
    }
}