# Demo API documents

## 회원 관련

* [회원 가입](./user/post.md): ```POST /api/v1/users```

## 세션 관련

* [로그인](./session/post.md): ```POST /api/v1/session```
* [로그아웃](./session/delete.md): ```DELETE /api/v1/session```

## 상품 관련

* [상품 목록 조회](./product/getAll.md): ```GET /api/v1/products```
* [상품 조회](./product/get.md): ```GET /api/v1/products/:id```
* [상품 주문](./product/order/post.md): ```POST /api/v1/products/:id/orders```

## 주문 관련

* [주문 조회](./order/getAll.md): ```GET /api/v1/orders```

--- 

## 에러 형식

에러 발생시 response body 는 다음과 같은 json 형식을 갖는다.  

```json
{
    "id": 40100,
    "message": "Unauthorized",
    "detail": "Wrong password."
}
```

* id: 에러 구분자
* message: 에러 메시지
* detail: 상세 에러 발생 원인

모든 API 에서 발생할 수 있는 에러 케이스는 다음과 같다.

| id    | message               | description                           |
|-------|-----------------------|---------------------------------------|
| 40000 | Bad request           | 요청 형식 에러 (body, query, path variable) |
| 40100 | Unauthorized          | 인증 정보 에러 (쿠키 세션 정보)                   |
| 50000 | Internal server error | 서버 버그로 인한 에러                          |