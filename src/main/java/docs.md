# 🖥️ HTTP Content-Type 정리

API 요청을 보낼 때 가장 먼저 확인해야 하는 것은 `Content-Type`이다.  
이 값에 따라 요청 본문(`body`) 형식, Postman 설정 방식, OkHttp 코드 작성 방식이 달라진다.

---

### 1. 핵심 기준
- Content-Type이 뭐냐?에 따라 달라짐
  - application/json => JSON 형식
  - application/x-www-form-urlencoded => form 형식

### 2. application/json <br>
=> 요청 본문을 JSON 형태로 보내는 방식

```json
{
  "searchKeywordType": "CHANNEL_PRODUCT_NO",
  "channelProductNos": ["123456789"]
}
```

[특징]
- 구조가 복잡한 데이터 전송에 적합
- 객체, 배열, 중첩 데이터 표현 가능
- 상품 조회, 주문 조회, 검색 조건 전달 등에 자주 사용
- Postman 설정 : Body -> raw -> JSON

[OkHttp 예시]
```Java
// JSON 문자열 직접 생성
String json = """
{
  "searchKeywordType": "CHANNEL_PRODUCT_NO",
  "channelProductNos": ["123456789"]
}
""";

// application/json 형식의 RequestBody 생성
RequestBody body = RequestBody.create(
        json,
        MediaType.get("application/json")
);

// 요청 생성
Request request = new Request.Builder()
        .url("https://api.commerce.naver.com/external/v1/products/search")
        .post(body)
        .addHeader("Authorization", "Bearer " + token)
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json")
        .build();
```

### 3. application/x-www-form-urlencoded <br>
=> 요청 본문을 key=value 형태의 form 데이터로 보내는 방식

** 실제 전송 예시
`client_id=aaa&timestamp=123456789&grant_type=client_credentials&type=SELF`

[특징]
- 단순한 key-value 데이터 전달에 적합
- 주로 토큰 발급, 로그인, OAuth 요청에서 많이 사용
- JSON처럼 복잡한 구조를 보내기엔 불편함
- Postman 설정 : Body -> x-www-form-urlencoded

[OkHttp 예시]
```Java
// form 데이터 생성
RequestBody body = new FormBody.Builder()
        .add("client_id", clientId)
        .add("timestamp", String.valueOf(timestamp))
        .add("grant_type", "client_credentials")
        .add("client_secret_sign", clientSecretSign)
        .add("type", "SELF")
        .build();

// 요청 생성
Request request = new Request.Builder()
        .url("https://api.commerce.naver.com/external/v1/oauth2/token")
        .post(body)
        .addHeader("Content-Type", "application/x-www-form-urlencoded")
        .build();
```

### 4. 두 방식 차이 비교
| 항목           | application/json          | application/x-www-form-urlencoded |
| ------------ | ------------------------- | --------------------------------- |
| 데이터 형태       | JSON 객체                   | key=value                         |
| 구조 표현        | 복잡한 구조 가능                 | 단순 항목 전달                          |
| 배열/중첩 객체     | 가능                        | 사실상 불편                            |
| 주 사용처        | 조회 조건, 검색 조건, 주문/상품 API   | 토큰 발급, 로그인, OAuth                 |
| Postman 설정   | raw + JSON                | x-www-form-urlencoded             |
| OkHttp 작성 방식 | `RequestBody.create(...)` | `FormBody.Builder()`              |

### 5. OkHttp에서 왜 다르게 쓰는가
=> 둘 다 결국은 요청 본문(RequestBody)을 만드는 것이지만, 본문 형식이 다르기 때문에 생성 방식이 달라진다.

** JSON일 때
`RequestBody body = RequestBody.create(json, MediaType.get("application/json"));`

** Form일 때
`RequestBody body = new FormBody.Builder()
        .add("key", "value")
        .build();`

=> Request.Builder()가 아니라 body를 만드는 방식 차이

### 6. 실무에서 판단하는 순서
1. Method가 GET / POST / PUT / DELETE 중 무엇인지 확인
2. Content-Type 확인
3. Request Example 확인
4. Body가 JSON인지 form인지 판단
5. OkHttp 코드 작성

application/json = JSON 문자열 만들어서 보냄
application/x-www-form-urlencoded = key=value form으로 보냄

### 7. 최종 정리
(1) application/json
- JSON 형식으로 body 전송
- 구조가 복잡한 데이터에 적합
- RequestBody.create(...) 사용

(2) application/x-www-form-urlencoded
- key=value 형식으로 body 전송
- 단순 항목 전달에 적합
- FormBody.Builder() 사용

** 어떤 방식을 쓸지는 무조건 API 문서의 Content-Type을 보고 결정한다. **