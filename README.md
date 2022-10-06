# Demo server

## API 문서

API 문서는 [/docs](./docs/README.md) 폴더에 API 별로 정리가 되어있습니다.

## Requirements
* Java 11
* Gradle

--- 

## How to

### Test

```
gradle test
```

만약 ```gradle``` 설치 없이 실행하고 싶다면
```gradlew```  또는 ```gradlew.bat``` 파일을 사용해주세요.

### Run
먼저 이미지를 빌드 합니다.
```
docker build . --tag demo-server
```

그 다음 컨테이너를 실행시켜주세요.
```
docker run -p 8080:8080 demo-server
```

DB 데이터를 컨테이너 외부에 저장하고 싶다면 다음과 같이 적절히 경로를 맵핑해주세요.

```
docker run -p 8080:8080 \
    -v ~/db:/db \
    demo-server
```

그리고 access log 를 컨테이너 바깥에 저장하고 싶다면 다음과 같이 적절한 경로로 맵핑해주세요.

```
docker run -p 8080:8080 \
    -v ~/db:/db \
    -v ~/logs:/logs \
    demo-server
```

### Run (without Docker)

```
gradle clean bootRun -x test
```

만약 ```gradle``` 설치 없이 실행하고 싶다면
```gradlew```  또는 ```gradlew.bat``` 파일을 사용해주세요.

---

## 코드 설명

### 패키지 구조

패키지는 크게 ```controller```, ```domain```, ```system``` 으로 나누어져 있습니다.
* `controller`: REST API 등 외부 통신을 담당하는 부분
* `domain`: 도메인 로직을 구현한 패키지
* `system`: 도메인 로직과 관련이 적은 spring boot framework 의 config, resolver, component 등을 포함

패키지 간의 의존관계는 `controller` > `domain (UseCase)` > `domain (Entity)` 순으로 이어지도록 했고, `system` 클래스는 최대한 다른 클래스가 참조하지 않도록 했습니다.

`domain` 패키지는 use case 클래스와 domain entity 클래스로 이루어져 있습니다. 여기서 use case 는 말 그대로 사용자 측면에서 바라본 프로그램의 기능을 의미합니다. 
따라서 use case 클래스는 다른 use case 클래스를 참조하거나 의존하지 않도록 규칙을 정했습니다.  

### 설정

설정은 [src/main/resources](./src/main/resources) 에 yaml 형식으로 저장하고 있습니다.
운영(prod), 개발(dev), 테스트(test) 환경별로 파일을 나누어서 관리하고 있으며 현재 기본 설정은 [application-prod.yaml](./src/main/resources/application-prod.yaml) 
을 사용하고 있습니다.  
유저 로그인 세션은 spring session 을 사용해서 관리했습니다. 일반적으로는 redis, memcached 등에 세션을 저장하지만,
데모용 서버는 데이터베이스에 저장하도록 설정했습니다.  
데모용 서버이므로 H2 데이터베이스를 사용하도록 설정했습니다. 서버가 종료된 뒤에도 데이터를 유지하고 싶으실 수도 있기 때문에 운영 및 개발 환경에서는 메모리가 아닌 파일에 저장할 수 있도록 했습니다.  
Access log 는 stdout 이 아닌 별도 파일에 기록하도록 설정했습니다.

설정값은 대부분 spring 및 undertow 에서 제공하는 값들을 사용하고 있지만, 커스텀 설정도 추가했습니다.

* ```secret.aes-key```: 비밀번호 암복호화 키.