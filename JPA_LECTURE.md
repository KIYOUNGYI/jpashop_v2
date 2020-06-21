# JPA를 왜 쓰지?
지금 시대는 객체를 관계형 DB에 관리!

SQL 중심 개발 문제 -> 무한반복,지루한 코드, CRUD (SQL 에 의존적인 개발은 피하기 어렵다)
패러다임 불일치 -> 객체 vs 관계형 db

객체를 영구 보관하는 다양한 저장소
Object -> RDB, NOSQL, FILE … 이런 옵션이 있는데 현실적인 대안은 RDB

객체 — <SQL 변환> — [sql] RDB

개발자 == sql 매퍼

객체와 rdb 차이
1.상속 (객체 상속관계 vs 테이블 슈퍼타입)
2.연관관계 
3.데이터타입
4.데이터식별방법


## 1.상속 (객체 상속관계 vs 테이블 슈퍼타입)
                    
			 Item
| 			|                |
Album       movie.       Book

앨범을 저장하려면 1] 객체 분해 -> 2] insert into item -> 3] insert into album

앨범을 조회하려면 1]  조인 sql 작성 -> 2] 각각 객체 생성 -> … 복잡 -> 보통 *DB* 에 저장할 객체에는 *상속 관계 안쓴다.*

자바 컬렉션에서 조회하면 정말 simple 해지는데…
```java
list.add(album);
// 자바 컬렉션에서 조회
Album album = list.get(albumId);
// 부모 타입으로 조회 후 다형성 활용
Item item = list.get(albumId);
```


## 2.연관관계
객체는 *참조* 사용 member.getTeam()
테이블은 *외래키* 사용 JOIN ON M.TEAM_ID = T.TEAM_ID

[객체 연관관계]
Member                                                      Team
Id, Team team, userName       —>           id,name

잘 보면 객체관계에서는 멤버에서 팀으로 갈 수는 있어도, *팀에서 멤버로 갈 수는 없음*
왜냐하면 참조가 없기 때문

반면 테이블은 갈 수 있음.

보통은 객체를 테이블에 맞추어 모델링

==> 객체지향 스럽지 못한 스타일
```java 
class Member { 
String id; //MEMBER_ID 컬럼 사용 
Long teamId; //TEAM_ID FK 컬럼 사용 //** 
String username;//USERNAME 컬럼 사용 
} 
class Team { 
Long id; //TEAM_ID PK 사용 
String name; //NAME 컬럼 사용 
} 

...

INSERT INTO MEMBER(MEMBER_ID, TEAM_ID, USERNAME) VALUES ... 

```



```java
class Member { 
String id; //MEMBER_ID 컬럼 사용 
Team team; //참조로 연관관계를 맺는다. //** 
String username;//USERNAME 컬럼 사용 
￼
Team getTeam() { return team; 
￼
} 
￼
} 
class Team { 
Long id; //TEAM_ID PK 사용 
String name; //NAME 컬럼 사용 
} 

```


### 객체 모델링 조회
```java
SELECT M.*, T.* FROM MEMBER M 
JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID 


public Member find(String memberId) { 
//SQL 실행 ... 
Member member = new Member(); 
//데이터베이스에서 조회한 회원 관련 정보를 모두 입력 
Team team = new Team(); 
//데이터베이스에서 조회한 팀 관련 정보를 모두 입력 
//회원과 팀 관계 설정 
member.setTeam(team); //** 
return member; 
} 
```

#### >>> 보통 슈퍼 dto 만들고 ex> member_team 뭐 이런식으로 그 dto 에 다 때려넣는 방식이 의외로 생산성이 좋다.

### 객체 모델링, 자바 컬렉션에 관리
```java
list.add(member); 
Member member = list.get(memberId); 
Team team = member.getTeam(); 

```


## 그래프 탐색
객체는 자유롭게 그래프를 탐색할 수 있어야 한다.
#### >>> 그렇다고 모든 걸 자유롭게 탐색할 수 있는 것은 아님 왜냐하면 처음에 날리는 쿼리가 어떤지에 따라 탐색 범위가 결정이 나버린다.

Member — Team          Category
|                                         |
Order — OrderItem — Item 
|
Delivery


```java
SELECT M.*, T.* FROM MEMBER M 
JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID 

member.getTeam(); // ok 
member.getOrder();// null
```


#### >>> 그래서 엔티티의 신뢰문제도 있음, find 까서 봐야 이게 진짜로 가져올 수 있는지 확인가능

```java 
class MemberService 
{ 
... 
	public void process() 
	{ 
		Member member = memberDAO.find(memberId);
		member.getTeam(); //??? 
		member.getOrder().getDelivery(); // ??? 
	} 
} 

```


## 모든 객체를 미리 로딩할 수 없다. 
```java

memberDAO.getMember(); //Member만 조회 memberDAO.getMemberWithTeam();//Member와 Team 조회 
￼//Member,Order,Delivery
memberDAO.getMemberWithOrderWithDelivery();

``` 


#### 계층형 아키텍처, 진정한 의미의 *계층 분할*이 어렵다. 


## 비교
```java
String memberId = "100";
Member member1 = memberDAO.getMember(memberId);
Member member2 = memberDAO.getMember(memberId);
member1 == member2; //다르다. 

class MemberDAO { 
public Member getMember(String memberId) { 
String sql = "SELECT * FROM MEMBER WHERE MEMBER_ID = ?";
... 
//JDBC API, SQL 실행 
return new Member(...); 
} 
￼ 

```

자바 컬렉션에서 조회 
```java
String memberId = "100";
Member member1 = list.get(memberId);
Member member2 = list.get(memberId);
member1 == member2; //같다. 

```


#### >>>> 객체답게 모델링 할수록 매핑 작업만 늘어난다. 
#### >>>> 객체를 자바 컬렉션에 저장 하듯이 DB에 저장할 수는 없을까? 

##### >>>> 그래서 나온 것이 JPA (Java Persistence API)


# ORM

자바 진영의 *ORM*기술 표준 

Object Relational mapping -> 객체는 객체대로 설계하고, Relational(관계형 db)는 관계형 db대로 설계하고, 이걸 orm 이 중간에서 매핑을 해주겠다 이게 ORM 프레임워크의 그림 (대중적 언어에는 다 이런 ORM 기술 존재함)

JPA 는 애플리케이션과 JDBC 사이에서 동작

## JPA 동작 방식
결론부터 애기하면 jdbc api 사용해서 통신함.

### 저장 
MemberDAO -> persist ->  (1) Entity 분석
                                                (2)  insert sql 생성 -> 개발자가 작성(x) 
						(3) JDBC API 사용 
						(4) 패러다임 불일치 문제 해결 -> *중요*
### 조회

MemberDAO -> find(id) -> (1) select sql 생성
						(2) JDBC API 사용
						(3) ResultSet 매핑
						(4) 패러다임 불일치 문제 해결 -> *중요*

## 역사
JPA 는 인터페이스 모음
구현체는 Hibernate, 외 이것저것


## JPA를 왜 사용해야 하는가? 
### SQL 중심적인 개발에서 객체 중심으로 개발 - 생산성
```java 
//저장
jpa.persist(member) 
//조회
Member member = jpa.find(memberId) 
//수정
member.setName(“변경할 이름”)
//삭제 jpa.remove(member) 
```

### 유지보수
기존: 필드 변경시 모든 SQL 수정 
JPA: 필드만 추가하면 됨, SQL은 JPA가 처리 


### *패러다임의 불일치 해결*

1.JPA와 상속

#### 저장
```java
//개발자가 할 일
jpa.persist(album);
//나머진 jpa 가 알아서 함
INSERT INTO ITEM ... 
INSERT INTO ALBUM ... 

```

#### 조회
```
개발자가 할일 
Album album = jpa.find(Album.class, albumId);
나머진 JPA가 처리 
￼
SELECT I.*, A.* 
￼FROM ITEM I 
￼JOIN ALBUM A ON I.ITEM_ID = A.ITEM_ID 
```

2.JPA와 연관관계 

```java
member.setTeam(team);
jpa.persist(member);// 영구저장


```

3.JPA와 객체 그래프 탐색 
```java
Member member = jpa.find(Member.class, memberId);
Team team = member.getTeam();
```

엔티티를 신뢰할 수 있음!!!
```java 
class MemberService 
{ 
... 
	public void process() 
	{ 
		Member member = memberDAO.find(memberId);
		member.getTeam(); //자유로운 객체 그래프 탐색 
		member.getOrder().getDelivery();
	} 
} 

```

4.JPA와 비교하기 (동일한 트랜잭션에서 조회한 엔티티는 같음을 보장)
```
String memberId = "100";
Member member1 = jpa.find(Member.class, memberId);
Member member2 = jpa.find(Member.class, memberId);
member1 == member2; //같다. 
```

### 성능

#### >>>> jpa 를 쓰게되면 성능 걱정할 수 있는데, 이런 중간계층이 있으면 할 수 있는 2가지가 있음 1> 모아서 쏘는 버퍼링 2> 읽을 때 캐싱하는 것 (이건 메모리나 cpu 도 마찬가지)


[1] 1차 캐시와 동일성(identity) 보장

```java 
/**
* 1차 캐시와 동일성 보장 * 1. 같은 트랜잭션 안에서는 같은 엔티티를 반환 - 약간의 조회 성능 향상 
* 2. DB Isolation Level이 Read Commit이어도 애플리케이션에서 Repeatable * Read 보장 
￼**/

String memberId = "100";
Member m1 = jpa.find(Member.class, memberId); //SQL
Member m2 = jpa.find(Member.class, memberId); //캐시 
println(m1 == m2) //true

```

#### >>>> 뭐 캐싱 기능으로 약간의 성능 도움이 있다긴 하는데 실무관점에서 볼 때 그렇게까지 큰 이득은 없다.

[2] 트랜잭션을 지원하는 쓰기 지연(transactional write-behind) 

```java

/**
 * 1. 트랜잭션을 커밋할 때까지 INSERT SQL을 모음 
 * 2. JDBC BATCH SQL 기능을 사용해서 한번에 SQL 전송 
 **/

transaction.begin(); // [트랜잭션] 시작 
￼
em.persist(memberA);
em.persist(memberB);
em.persist(memberC);
//여기까지 INSERT SQL을 데이터베이스에 보내지 않는다. 
//커밋하는 순간 데이터베이스에 INSERT SQL을 모아서 보낸다. 
transaction.commit(); // [트랜잭션] 커밋 

```


Update
```java
/**
* 1. UPDATE, DELETE로 인한 로우(ROW)락 시간 최소화 
* 2. 트랜잭션 커밋 시 UPDATE, DELETE SQL 실행하고, 바로 커밋 
**/
transaction.begin(); // [트랜잭션] 시작 
￼
changeMember(memberA);
deleteMember(memberB);
비즈니스_로직_수행(); //비즈니스 로직 수행 동안 DB 로우 락이 걸리지 않는다. 
//커밋하는 순간 데이터베이스에 UPDATE, DELETE SQL을 보낸다. 
transaction.commit(); // [트랜잭션] 커밋 

```

[3] 지연 로딩(Lazy Loading)과 즉시로딩(Eager Loading)

지연로딩
```java 
Member member = memberDAO.find(memberId);//SELECT * FROM MEMBER
Team team = member.getTeam();
String teamName = team.getName();//SELECT * FROM TEAM
￼
```


* 즉시 로딩 -  JOIN SQL로 한번에 연관된 객체까지 미리 조회 

```java
Member member = memberDAO.find(memberId);// ￼ select M.*, T.* FROM MEMBER JOIN TEAM ...

Team team = member.getTeam();
String teamName = team.getName();
```

#### >>>> 보통 lazy 설정해놓은다음 최적화가 필요한 경우 그때 손을 댄다. 사실 위와 같은 예제를 코드로 직접 한다고 치면, 정말 코드를 갈아 넣어야 하는데(손이 많이 가는데) JPA를 사용한다면, 옵션 하나 키고 끄고로 해결할 수 있다(튜닝). 멤버조회할 때 무조건 팀을 가져온다, 그러면 즉시로딩이 이득이겠지, 그런데 뭐 어쩌다 팀이 필요한 경우에는 지연로딩 세팅이 이득일테고, 그래도 실무에서는 지연로딩이 정석이다. 그다음에 필요하면 튜닝하고

#### >>>> 결론 : orm 은 객체와 rdb 두 기둥위에 있는 기술 (둘다 잘해라) 



# 영속성 관리( JPA 내부 구조)
*영속성 컨텍스트*
```
* JPA를 이해하는데 가장 중요한 용어 
* “엔티티를 영구 저장하는 환경”이라는 뜻 
* EntityManager.persist(entity);//사실은 db에 저장하는 것이 아니라 엔티티를 영속성 컨텍스트에 저장한다는 뜻이다.

* 영속성 컨텍스트는 논리적인 개념이다
* 눈에 안보인다
* 엔티티 매니저를 통해서 영속성 컨텍스트에 접근
```

#### >>>> 중요 : 사실은 db에 저장하는 것이 아니라 엔티티를 영속성 컨텍스트에 저장한다는 뜻이다. (entityManager 안에 영속성 컨텍스트 있다고 보면 된다)

## 생명주기
비영속 (new/transient) - 영속성 컨텍스트와 전혀 관계가 없는 *새로운*상태 
영속(managed) - 영속성 컨텍스트에 *관리*되는 상태 
준영속 (detached) - 영속성 컨텍스트에 저장되었다가 *분리*된 상태 
삭제 (removed) - 삭제된 상태 



비영속 - 객체를 생성한 상태

```java
Member member = *new*Member(); 
member.setId(“member1”);
member.setUsername(“회원1”); 
```

영속
```java
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
//객체를 저장한 상태(영속) 
em.persist(member);

//db 에 저장되는 시점은 commit 될 때 
tx.commit();
```

준영속, 삭제
```java
//회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태 
em.detach(member);

//객체를 삭제한 상태(삭제) 
em.remove(member);
```


#### >>>> 이런 메커니즘을 두는 이유가 무엇일까??? 얻는 이점이 뭐지????
#### >>>> 버퍼링, 캐싱 

1차 캐시 동일성(identity) 보장 
트랜잭션을 지원하는 쓰기 지연
 (transactional write-behind) 
변경 감지(Dirty Checking) 지연 로딩(Lazy Loading) 
