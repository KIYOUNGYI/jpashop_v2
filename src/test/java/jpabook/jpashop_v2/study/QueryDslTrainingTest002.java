package jpabook.jpashop_v2.study;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop_v2.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static jpabook.jpashop_v2.domain.QMember.member;


@SpringBootTest
@Transactional
public class QueryDslTrainingTest002
{
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;//필드로 빼서 사용하는 것 권장

    @BeforeEach
    public void before()
    {
        queryFactory = new JPAQueryFactory(em);
    }

    /**
     *
     * select member0_.member_id as member_i1_4_, member0_.address as address2_4_,
     * member0_.street as street3_4_, member0_.zipcode as zipcode4_4_,
     * member0_.age as age5_4_, member0_.name as name6_4_,
     * member0_.team_id as team_id7_4_
     * from member member0_
     * where member0_.name='user_r'
     * and member0_.age=18;
     *
     * select member0_.member_id as member_i1_4_, member0_.address as address2_4_,
     * member0_.street as street3_4_, member0_.zipcode as zipcode4_4_,
     * member0_.age as age5_4_, member0_.name as name6_4_, member0_.team_id as team_id7_4_
     * from member member0_
     * where member0_.name='user_r';
     * @throws Exception
     */
    @Test
    public void 동적쿼리_BooleanBuilder() throws Exception {
        String usernameParam = null;
        Integer ageParam = 18;
        System.out.println("===== start ====");
        List<Member> result = searchMember1(usernameParam, ageParam);
        System.out.println("===== end ======");
//        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 동적쿼리_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;
        List<Member> result = searchMember2(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String userNameCond, Integer ageCond)
    {
        BooleanBuilder builder = new BooleanBuilder();
        if(userNameCond!=null)builder.and(member.name.eq(userNameCond));
        if(ageCond!=null)builder.and(member.age.eq(ageCond));

        return queryFactory.selectFrom(member).where(builder).fetch();
    }



    private List<Member> searchMember2(String usernameParam, Integer ageParam)
    {
        List<Member> result = queryFactory.selectFrom(member)
                                          .where(userNameEq(usernameParam), ageEq(ageParam))
                                          .fetch();
        return result;
    }

    private BooleanExpression userNameEq(String usernameParam)
    {
        if(usernameParam!=null) return member.name.eq(usernameParam);
        else return null;
    }

    private BooleanExpression ageEq(Integer ageParam)
    {
        return ageParam!=null ? member.age.eq(ageParam):null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return userNameEq(usernameCond).and(ageEq(ageCond));
    }


//    수정, 삭제 벌크 연산
    /**
     * 쿼리 한번으로 대량 데이터 수정
     */
    public void f1()
    {
        long count = queryFactory
                .update(member)
                .set(member.name, "비회원")
                .where(member.age.lt(28))
                .execute();
    }
    /**
     * 기존 숫자에 1 더하기
     * update member
     *  set age = age + 1
     */
    public void f2()
    {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    /**
     * 쿼리 한번으로 대량 데이터 삭제
     * long count = queryFactory
     *  .delete(member)
     *  .where(member.age.gt(18))
     *  .execute();
     * 주의: JPQL 배치와 마찬가지로, 영속성 컨텍스트에 있는 엔티티를 무시하고 실행되기 때문에 배치 쿼리를
     * 실행하고 나면 영속성 컨텍스트를 초기화 하는 것이 안전하다.
     */
    public void f3()
    {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }


    /**
     * SQL function 호출하기
     * SQL function은 JPA와 같이 Dialect에 등록된 내용만 호출할 수 있다.
     * member M으로 변경하는 replace 함수 사용
     */
    public void f4()
    {
        String result = queryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.name, "member", "M"))
                .from(member)
                .fetchFirst();

//        JPAQuery<Member> memberJPAQuery = queryFactory.select(member).from(member).where(member.name.eq(member.name.lower()));
    }
    //소문자로 변경해서 비교해라.
    //.select(member.username)
    //.from(member)
    //.where(member.username.eq(Expressions.stringTemplate("function('lower', {0})",
    //member.username)))
    //lower 같은 ansi 표준 함수들은 querydsl이 상당부분 내장하고 있다. 따라서 다음과 같이 처리해도 결과
    //는 같다.
    //.where(member.username.eq(member.username.lower()))

    

}
