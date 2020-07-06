# jpashpop_v2

강좌 내용

엔티티는 가급적 setter 를 사용하지 말아라
-> 유지보수가 너무 어려워진다.

모든연관관계는 지연로딩으로 설정해라!
-> 수많은 장애를 극복할 수 있다!!!
즉시(EAGER)은 예측이 어렵고, 어떤 sql이 실행될 지 추적이 어렵다.
특히, jpql 사용시, n+1 문제가 발생할 확률이 크다.

실무에서 모든 관계는 LAZY 로 설정해야 한다.

@ManyToOne(fetch = FetchType.LAZY)<- 이거 꼭 해야 한다. 안하면 난리난다 진짜.
oneToOne 도 마찬가지
static import 단축키 (option+enter)

대안] 연관된 엔티티를 함께 db 에서 조회해야 하면, fetch join 또는 엔티티 그래프를 사용한다.

컬렉은 필드에서 초기화 하자. 
하이버네이트 문서에서 베스트 프랙티스는 필드에서 초기화 하는 것이다. 
null 문제에서 안전하다. 

하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한 다. 만약 getOrders() 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문 제가 발생할 수 있다. 따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.


Member member = new Member();<br>
System.out.println(member.getOrders().getClass());<br>
em.persist(team);<br>
System.out.println(member.getOrders().getClass());<br>

//출력 결과

class java.util.ArrayList<br>
class org.hibernate.collection.internal.PersistentBag

칼럼명은 spring physical naming strategy 를 따른다. 

cascade(<- 학습 필요)

원래
persist(orderItemA)<br>
persist(orderItemB)<br>
persist(orderItemC)<br>
persist(order)<br>

cacade = cacadeType.ALL<br>
persist(order)<br>

연관관계 메소드

회원기능
- 회원등록
- 회원조회

상품기능
- 상품등록
- 상품수정
- 상품조회

주문기능
- 상품주문
- 주문내역조회
- 주문취소

다음은 제외(일단)
- 로그인 권한관리 (x)
- 파라미터 검증과 예외처리 단순화(x)
- 상품은 도서만 사용
- 카테고리 사용(x)
- 배송정보 사용(x)

아키텍처 : 계층형
- controller,web : 웹 계층
- service : 비즈니스 로직(트랜잭션 처리)
- repository : jpa 사용, 엔티티메니저 사용
- domain : 엔티티 모여있는 계층, 모든 곳에서 사용

서비스 -> 레포지토리 계층 -> 테스트케이스 작성 -> 웹 계층 개발

단방향(좀, 유연하게 컨트롤러에서 레포지토리에 접근도 경우에 따라선 가능하게끔)

테스트케이스 작성
회원가입 성공해야함
회원가입할 때 같은 이름 있으면 예외발생


테스트 돌릴 때 외부 db 안돌리고 내부 db를 공짜로 돌릴 수 있는 방법이 있다???

========== 조회 성능 최적화 ===========================================================

[1] 조회용 샘플 데이터 입력
[2] 지연 로딩과 조회 성능 최적화
[3] 컬렉션 조회 최적화
[4] 페이징과 한계 돌파
[5] osiv와 성능 최적화

===========================
1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
2. 필요하면 페치 조인으로 성능을 최적화 한다. 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사
용한다.


=========== QueryDSL =============
[1] 설정
[2] intellij > 우측 Gradle > Tasks > other > compileQuerydsl  더블클릭시 build 디렉토리 > querydsl > Q로 시작하는 자바파일 생성됨.
[3] ./gradlew clean
[4] 뷸드된 파일들은 깃으로 관리하면 안됨


fetchREsult -> 페이징 쿼리가 복잡해지면
성능이 중요한 것은 쿼리를 두 방 날려야 한다.


git cache issue
$ git rm -r --cached .
$ git add .
$ git commit -m "fixed untracked files”

연관관계 없어도 join 가능하다.(ceta join) - a.k.a (막조인)
제약 : left join, left outer join 등 외부조인 불가능


동적 쿼리 - BooleanBuilder 사용
동적 쿼리를 해결하는 두가지 방식
[1] BooleanBuilder
[2] Where 다중 파라미터 사용


JPAQueryFactory 스프링 빈 등록
다음과 같이 JPAQueryFactory 를 스프링 빈으로 등록해서 주입받아 사용해도 된다.
@Bean
JPAQueryFactory jpaQueryFactory(EntityManager em) {
return new JPAQueryFactory(em);
}
참고: 동시성 문제는 걱정하지 않아도 된다. 왜냐하면 여기서 스프링이 주입해주는 엔티티 매니저는 실제 동
작 시점에 진짜 엔티티 매니저를 찾아주는 프록시용 가짜 엔티티 매니저이다. 이 가짜 엔티티 매니저는 실제
사용 시점에 트랜잭션 단위로 실제 엔티티 매니저(영속성 컨텍스트)를 할당해준다.

명령어
./gradlew compileQuerydsl


사용자 정의 리포지토리
사용자 정의 리포지토리 사용법
1. 사용자 정의 인터페이스 작성
2. 사용자 정의 인터페이스 구현
3. 스프링 데이터 리포지토리에 사용자 정의 인터페이스 상속

command + option + m => 메소드 추출


값 타입 중요한 것은

1] 임베디드 타입(복합 값 타입)
2] 값 타입 컬렉션 

이 두가지가 중요. 나머지는 걍 그러려니 하고 보면 됨

값 타입 분류
기본값 타입
* 자바 기본 타입(int,double)
* 래퍼 클래스(Integer,Long)
* String

임베디드 타입(embedded type, 복합 값 타입) -> 우편번호, 좌표 뭐 이런거 포지션 클래스 만들어서 쓰는거
컬렉션 값 타입(collection value type) -> 자가 기본태입이나 임데디드 타입 넣을 수 있는 

int, double 같은 기본 타입(primitive type) 은 절대 공유x

기본 타입은 항상 값을 복사함

Integer 같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경 x

