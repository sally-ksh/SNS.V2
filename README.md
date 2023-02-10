## SNS V2

- skills
    - Java 11
    - Spring Boot 2.7.8
    - JPA
    - MySQL v8
    - Redis v6
    - Spring Security (a little)
- 오픈소스
    - [java-jwt](https://github.com/auth0/java-jwt) 이용한 JWT 토큰 생성 및 검증 로직 구현
- 외부 API 연동
    - [카카오 로컬 API](https://developers.kakao.com/docs/latest/ko/local/dev-guide)

```
    실행환경
    - 도커 기반 로컬 개발 환경 (2023.02)
      - 도커 : .env 파일을 루트 경로에 추가 하여 환경변수 이용
      - .env-ex 참고
    - 로컬 : IDE 에 환경 변수 별 값 입력
      - JPA auto ddl 이용한 DB 테이블 생성
      - 실행 `docker-compose -f docker-compose-local.yml up`

```

---

### 시퀀스 다이어그램

- SNS V1에서의 JWT 토큰 만료기간 30일 내 로그인 사용자 DB 접근 비용 감소 위해 캐싱 추가

![SNS V2 Redis - login](https://user-images.githubusercontent.com/96989782/217999145-44583dee-65be-4bdd-82e6-966fc2e6174e.png)

![SNS V2 Redis - address](https://user-images.githubusercontent.com/96989782/217998936-ba851f15-2a68-453e-aaaa-b9befdff5614.png)


<br>

---

카카오 주소 검색

GET `http://localhost:8080/api/v2/sns/addresses?address=전북 삼성동 100`

``` json
{
    "resultCode": "SUCCESS",
    "result": {
        "meta": {
            "total_count": 3
        },
        "documents": [
            {
                "address_name": "전북 익산시 부송동 100",
                "x": 126.99597495347,
                "y": 35.9766482774579
            },
            {
                "address_name": "전북 익산시 임상동 100",
                "x": 126.980268573424,
                "y": 35.9816612949055
            },
            {
                "address_name": "전북 익산시 정족동 100",
                "x": 127.002020445866,
                "y": 35.9829740190924
            }
        ]
    }
}
```

<br>
<br>

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fgjbae1212%2Fhit-counter)](https://hits.seeyoufarm.com)                    

