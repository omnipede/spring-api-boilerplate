# 상품 목록 조회

**Method**: ```GET```  
**URL**: ```/api/v1/products```  
**Query**:

* page: 페이지 번호 (0 부터 시작)
* size: 페이지 크기

**Body**:  

```json
{
    "total": 3,
    "data": [
        {
            "id": 1,
            "name": "Monitor",
            "price": 100000
        },
        {
            "id": 2,
            "name": "HDD",
            "price": 50000
        },
        {
            "id": 3,
            "name": "SSD",
            "price": 80000
        }
    ]
}
```

* total: 전체 상품 수
* data: 페이지에 포함된 상품 목록
* data[].id: 상품 아이디
* data[].name: 상품명
* data[].price: 상품 가격
