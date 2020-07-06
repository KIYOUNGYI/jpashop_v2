# 사용자 정의 레포지토리 
1] MemberRepositoryImpl <- 이름은 MemberRepository 에 Impl 을 붙여줘야 함. 이건 룰임
2] MemberRepositoryCustom

JPAQUERYFACTORY <- querydsl 쓸려면

TIP] 조회쿼리가 너무 복잡하면 그냥 Repository Class 로 뽑아내라. 
뭐 MemberQueryRepository 만들고
@Repository 어노테이션 달고
 
모든걸 Custom 에 때려박는것도 좋은 설계는 아니다.

핵심 비즈니스 로직, 잘 사용할 수 있는 건, Custom 에다 넣고,
Entity 를 찾는경우는 MemberRepository 에 넣고, 
전혀 공용성이 없고, 이건 약간 뭔가 특정 api 에 종속되어 있거나, 그런건 수정 라이프사이클 자체가 api 화면 자체에 맞춰서 기능이 변경이 되거든요. 조회(특화된) 별도로 빼는 것도 나쁜 것은 아니다. 


Select 뭐시기는 쿼리 프로젝션하는거고
Where 절은 동적쿼리 


# QueryProjection

장점
1] 안전한다 <- 컴파일 시점에 잡아주니까
단점
1] Q파일 생성 (의존성 생겨, 쿼리dsl 이나 라이브러리 임포트에 대한…) 만약 쿼리디에셀 빼면 영향을 받겠죵
2] 길어지면 힘들어
3] 여러 layer 에 걸쳐서 돌아다니는데, 레포지토리에서 dto 조회하면, 서비스에서도 쓰고, 컨트롤러에서도 사용하는데, 심지어 api 로 바로 반환하기도 하는데, 
그럼 dto 안에 쿼리프로젟현이 들어있는건데 이게 순수하지 않은거죠

4] 만약에 깔끔하게 가고 싶으면, 필드나 빈 방식을 사용하면 되고, 그게 아니고 쿼리프로젝션 사용 많이사용하니까, 이정도는 그냥 가자 하면 쿼리 프로젝션 


=====================서적 내용========================

결과를 DTO 반환할 때 사용 다음 3가지 방법 지원 
* 프로퍼티 접근 필드 
* 직접 접근 
* 생성자 사용

프로퍼티 접근
```java
List result = queryFactory
.select(Projections.bean(MemberDto.class, 
							member.username, 
							member.age)) 
.from(member) 
.fetch();
```

직접 접근
```java
List result = queryFactory
.select(Projections.fields(MemberDto.class, 
						      member.username,    		               member.age)) 
.from(member) 
.fetch();
```

별칭이 다를 땐
* ExpressionUtils.as(source,alias) : 필드나, 서브 쿼리에 별칭 적용
* username.as(“memberName”) : 필드에 별칭 적용


생성자 사용
```java
List result = queryFactory
.select(Projections.constructor(MemberDto.class, member.username, member.age))
.from(member)
.fetch(); 
}
```


프로젝션과 결과 반환 - @QueryProjection
생성자 + @QueryProjection
```java
@Data public class MemberDto 
{ 
private String username; 
private int age; 
public MemberDto() { } 

@QueryProjection public MemberDto(String username, int age) { this.username = username; 
  this.age = age; 
} 

}
```

@QueryProjection 활용
```java
List result = queryFactory 
.select(new QMemberDto(member.username,member.age))
.from(member)
.fetch();
```

이 방법은 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법이다.

 다만 DTO에 QueryDSL 어노테 이션을 유지해야 하는 점과 DTO까지 Q 파일을 생성해야 하는 단점이 있다.