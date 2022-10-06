# 회원 가입

신규 회원 추가

**Method**: ```POST```  
**URL**: ```/api/v1/users```  
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

## Error response

### 중복된 이메일로 가입한 경우

**Code**: 409  
**Body**:

```json
{
    "id": 40900,
    "message": "Conflict",
    "detail": "User omnipede@naver.com already exists."
}
```