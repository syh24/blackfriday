## 소개

**Blackfriday**

무신사 블랙프라이데이 이벤트때 이벤트 주문에 대해서 대용량 트래픽에 어떻게 대응하는지 궁금해서 시작한 개인 프로젝트입니다. <br>

## 개발 기간
* 24.07 ~

## 기술스택
- **Language**: `Java 21`
- **Framework** : Springboot(3.3.1)
- **Database** : MySQL
- **ORM** : JPA

## 개발 내용
### 로그인/회원가입
- 회원가입
- 로그인 기능 (Spring Security + JWT 토큰 활용)

### 다양한 이벤트 주문 재고 처리
- synchronized 이용한 동기 처리
- LettuceLock을 이용한 동기 처리
- Redisson을 이용한 동기 처리
- Redis + RabbitMQ를 이용한 비동기 처리

### 문서
- RestDoc을 이용한 API 문서 정리

## 블로그
[무신사 블프 이벤트 상품 동시성 문제 (1)](https://green-dev.tistory.com/1) <br>
[무신사 블프 이벤트 상품 동시성 문제 (2)](https://green-dev.tistory.com/2) <br>
[AOP를 이용한 분산락 구현](https://green-dev.tistory.com/4) <br>
[Spring Security를 이용하여 JWT 토큰방식 로그인 구현](https://green-dev.tistory.com/6) <br>
[[트러블 슈팅] docker-compose로 브릿지 네트워크 구성](https://green-dev.tistory.com/7) <br>
