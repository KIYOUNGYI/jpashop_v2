# Proxy 
## 프록시 기초
em.find() vs em.getReference()

em.find() -> 진짜 데이터를 가지고 오는 것이고 em.getReference() 는 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회 하는 것 (즉, 쿼리는 안나가는데, 객체가 조회되는 것이다)

```java
Team team = new Team("alpha");
Member member = new Member();
member.setName("Paul Yi");
member.setAge(31);
em.persist(team);
em.persist(member);
em.flush();
em.clear();

Member findMember  = em.getReference(Member.class,member.getId());
System.out.println(">>>>>>>> findMember.getId() : "+ findMember.getId());

```


Select 쿼리 실행 안함.
>>>>>>>> findMember.getId() : 4137

```java
Team team = new Team("alpha");
Member member = new Member();
member.setName("Paul Yi");
member.setAge(31);
em.persist(team);
em.persist(member);
em.flush();
em.clear();

Member findMember  = em.getReference(Member.class,member.getId());
System.out.println(">>>>>>>> findMember.getId() : "+ findMember.getId());
System.out.println(">>>>>>>> findMember.getUserName() : "+ findMember.getName());
```

Sql 쿼리 실행함 -> findMember.getName() 때문에, id 는 가지고 있음

껍데기는 똑같은데, 아이디만 갖고있고 내용물은 텅텅 빈 것이다.
프록시 특징
* 실제 클래스 상속받아 만들어짐 (하이버네이트가~)
* 클래스와 겉 모양이 같음
* 이론상 사용하는 입장에서 구분하지 않고 사용하면 되는 거란다…글쎄??
* 프록시 객체는 실제 객체의 참조(target) 보관
* 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출




1] 처음에 멤버타겟 값이 없음. 
2] 그러면 jpa 가 영속성 컨텍스트에 값을 요청.
3,4] 영속성 컨텍스트가 db를 조회해서 실제 엔티티 객체를 생성해서 줌.
5] 멤버를 타겟에다 연결을 시켜줌

## 프록시의 특징(중요)
* *프록시 객체는 한 번만 초기화*
* 프록시 객체가 실제 엔티티로 바뀌는 것은 아님. 초기화되면, 프록시 객체를 통해서 실제 엔티티에 접근 가능
* 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입체크시 주의해야함.(== 비교 대신 실패, 대신 instance of 사용). 타입비교는 절대 == 쓰면 안된당 !!!!!!!!!!
* 영속성 컨텍스트에 찾는 엔티티가 이미 있으면, em.getReference() 를 호출해도 실제 엔티티 반환 (중요한 건, jpa가 어떻게든 맞춰준다 ==) 
* 영속성 컨텍스트의 도움을 받을 수 없는 준 영속성 상태일 때, 프록시를 초기화 하면 문제 발생 ( 하이버네이트는 org.hibernate.LazyInitalizationException 예외 터짐


