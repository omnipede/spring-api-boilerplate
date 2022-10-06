# 로그인

로그인 세션 생성 API

**Method**: ```POST```  
**URL**: ```/api/v1/session```  
**Body**: 

```json
{
  "email": "omnipede@naver.com",
  "password": "password"
}
```

## Success response

**Code**: 200  
**Body**: (empty)  
**Cookies**:

| name    | example                                                                     |
|---------|-----------------------------------------------------------------------------|
| SESSION | SESSION=ZjFiZjE5ODItNDhjZi00YTYzLWEwY2ItMzcwMDhhNTFmZDBh; Path=/; HttpOnly; |

## Error response

### 로그인 정보 오류

**Code**: 401  
**Body**:

```json
{
    "id": 40100,
    "message": "Unauthorized",
    "detail": "Wrong password."
}
```