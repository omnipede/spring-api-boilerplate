# 상품 조회

**Method**: ```GET```  
**URL**: ```/api/v1/products/:id```  
**Path variable**:

* id: 상품 ID

**Body**: (empty)  

## Success response

**Code**: 200  
**Body**: 

```json
{
    "id": 2,
    "name": "HDD",
    "price": 50000
}
```

* id: 상품 ID
* name: 상품명
* price: 상품 가격

## Error response

### 존재하지 않는 상품인 경우

**Code**: 404
**Body**:

```json
{
    "id": 40400,
    "message": "Not found",
    "detail": "Product id 4 not found."
}
```