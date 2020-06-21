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
