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

![SNS V2 Redis](https://user-images.githubusercontent.com/96989782/217471490-939633a7-48e5-497c-92eb-64e8e3c14601.png)



<br>
<br>

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fgjbae1212%2Fhit-counter)](https://hits.seeyoufarm.com)                    

