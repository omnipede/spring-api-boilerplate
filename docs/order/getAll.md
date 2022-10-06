# 주문 목록 조회

회원 본인의 주문 목록을 조회할 수 있는 API.

**Method**: ```GET```  
**URL**: ```/api/v1/orders```  
**Query**: 

* page: 페이지 번호 (0부터 시작), optional.
* size: 페이지 크기, optional.

**Body**: (empty)  
**Cookies**:

| name    | description           |
|---------|-----------------------|
| SESSION | 로그인 API 를 호출하여 획득한 쿠키 |

## Success response

**Code**: 200  
**Body**: 

```json
{
    "total": 1,
    "data": [
        {
            "product": {
                "id": 1,
                "name": "Monitor",
                "price": 100000
            },
            "ordered_at": "2022-10-06 02:08:30"
        }
    ]
}
```

* total: 전체 주문 개수
* data: 페이지에 포함된 주문 목록
* data[].product: 주문한 상품
* data[].product.id: 주문한 상품 ID
* data[].product.name: 상품명
* data[].product.price: 상품 가격
* data[].ordered_at: 주문 시각