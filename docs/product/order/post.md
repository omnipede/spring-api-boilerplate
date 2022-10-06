# 상품 주문

**Method**: ```POST```  
**URL**: ```/api/v1/products/:id/orders```  
**Path variables**:

* id: 주문할 상품 ID

**Body**: (empty)

**Cookies**:

| name    | description           |
|---------|-----------------------|
| SESSION | 로그인 API 를 호출하여 획득한 쿠키 |

## Success response

**Code**: 200  
**Body**: (empty)

## Error response

### 존재하지 않는 상품인 경우

**Code**: 404  
**Body**: 

```json
{
    "id": 40400,
    "message": "Not found",
    "detail": "Product id 5 not found."
}
```