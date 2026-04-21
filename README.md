# NH_PK_DEV

## 1. 26/04/06 ~ 26/04/17
- API 호출 방식 : OkHttp3 Library 방식으로 통일
- <details>
   <summary>Postman Test 진행</summary>
   <img width="255" height="630" alt="image" src="https://github.com/user-attachments/assets/4f8ab7aa-1bc7-4f78-96b0-9ccdb486b2d7" />
 </details>


### (1) Naver Commerce API 🛒
API Docs : https://apicenter.commerce.naver.com/docs/introduction
<br>

* 1️⃣ 인증 토큰 발급 (GetToken)
  * https://apicenter.commerce.naver.com/docs/commerce-api/current/exchange-sellers-auth
* 2️⃣ 전자 서명 생성 (GenerateSignature)
  * https://apicenter.commerce.naver.com/docs/auth#%EC%A0%84%EC%9E%90%EC%84%9C%EB%AA%85
* 3️⃣ 상품 목록 조회 (UpdateChannelItem)
  * https://apicenter.commerce.naver.com/docs/commerce-api/current/search-product
* 4️⃣ 상품 주문 조회 (UpdateChannelOrder)
  * https://apicenter.commerce.naver.com/docs/commerce-api/current/seller-get-product-orders-with-conditions-pay-order-seller

<br>

### (2) CJ Delivery API 📦
API Docs : https://dxapi.cjlogistics.com/#default/gallery
<br>

* 1️⃣ 인증 토큰 발급 (GetToken)
  * https://dxapi.cjlogistics.com/#default/apiDetails/c.restObject.API-Portal.TLE7UYX0Eest2OSq6nvrqw.-1
* 2️⃣ 운송장 번호 생성 (GetInvoiceNo)
  * https://dxapi.cjlogistics.com/#default/apiDetails/c.restObject.API-Portal.xI3kwYX0Eest2OSq6nvrqw.-1
* 3️⃣ (일반) 예약 접수 (ReqBook)
  * https://dxapi.cjlogistics.com/#default/apiDetails/c.restObject.API-Portal.kYPPkYI-Eest2OSq6nvrqw.-1

<br>

### * Service Structure
- Legacy Structure : Scheduler -> Service -> Mapper(MyBatis) -> DB(Oracle)
- Legacy API Call : Naver (OkHttp3), CJ (RestTemplate)

| learn          | Legacy                                |
|----------------|---------------------------------------|
| Controller     | Controller / Scheduler                |
| Service        | Service                               |
| Repository     | Mapper.xml / SqlSessionTemplate       |
| DTO            | DTO / Map (HashMap<>())               |
| Domain(Entity) | VO / DTO / Map / JsonNode / 테이블 중심 객체 |
