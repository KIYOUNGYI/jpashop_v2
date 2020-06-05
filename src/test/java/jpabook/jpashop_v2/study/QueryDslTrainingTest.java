package jpabook.jpashop_v2.study;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.QMember;
import jpabook.jpashop_v2.domain.QTeam;
import jpabook.jpashop_v2.domain.Team;
import jpabook.jpashop_v2.dto.MemberDto;
import jpabook.jpashop_v2.dto.QMemberDto;
import jpabook.jpashop_v2.dto.UserDto;
import jpabook.jpashop_v2.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import java.util.List;

import static jpabook.jpashop_v2.domain.QMember.*;
import static jpabook.jpashop_v2.domain.QMember.member;
import static jpabook.jpashop_v2.domain.QTeam.team;
import static org.assertj.core.api.Assertions.*;

import static com.querydsl.jpa.JPAExpressions.select;

@SpringBootTest
@Transactional// EntitiyManager 활용하려면 이게 있어야 하네?
//@Commit
public class QueryDslTrainingTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;//필드로 빼서 사용하는 것 권장

    @BeforeEach
    public void before() {
        // 동시성 문제 없나? 없음 스프링 프레임워크가 주입해주는 엔티티 매니저가 멀티쓰레드에 아무 문제 없게 설계됨
        queryFactory = new JPAQueryFactory(em);//
//        dummy();

    }
    @Test
    private void dummy()
    {
        Team teamG = new Team("teamG");
        Team teamH = new Team("teamH");
        em.persist(teamG);
        em.persist(teamH);
        for (int i = 0; i < 26; i++) {
            Member member = null;
            if (i % 2 == 0) {
                member = new Member("user_" + (char) (97 + i), null, teamG, i + 1);
            } else {
                member = new Member("user_" + (char) (97 + i), null, teamH, i + 1);
            }
            em.persist(member);
        }

    }

    @Test
    public void startJPQL()
    {
        Member findMember = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "user_a").getSingleResult();
        System.out.println("findMember.toString():" + findMember.toString());
        assertThat(findMember.getName()).isEqualTo("user_a");
    }



    /**
     * jpql 은 파라미터 바인딩
     * querydsl 은 preparedstatement 로 한다
     * sql injection
     * =============== 어마어마한 이점 ======================
     * Jpql은 문자다 보니 오타를 runtime 때 발견
     * QueryDSL 은 compile 시점에 발견
     * ide 에서 오타나면 다 잡을 수 있음.
     */
    @Test
    public void startQueryDsl() {
        //[1]
//        queryFactory = new JPAQueryFactory(em);//
        QMember m = new QMember("dummy");//같은 테이블 선언해서 조인걸어야 할 때 별칭 필요, 그 외에는 필요 없음

        Member member = queryFactory.select(m)
                                    .from(m)
                                    .where(m.name.eq("user_a"))//parameter 바인딩
                                    .fetchOne();
        System.out.println("member:"+member.toString());
        assertThat(member.getName()).isEqualTo("user_a");
        //[2]
//        QMember m2 = QMember.member;//Q파일에 static 으로 선언한게 있더라
//        Member member2 = queryFactory.select(m2)
//                .from(m2)
//                .where(m2.name.eq("member1"))//parameter 바인딩
//                .fetchOne();

    }

    @Test
    public void startQueryDsl2()
    {
        //  스태틱 타고 들어가면 public static final QMember member = new QMember("member1");
        QMember qm = new QMember("dummy");
        //[3]
        Member findMember = queryFactory.select(qm)
                                        .from(qm)
                                        .where(qm.name.eq("user_a"))//바인딩 처리
                                        .fetchOne();
        System.out.println("findMember:"+findMember.toString());
        assertThat(findMember.getName()).isEqualTo("user_a");
    }


    @Test
    public void search()
    {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.name.eq("user_t")
                        .and(member.age.eq(20)))
                .fetchOne();
        assertThat(findMember.getName()).isEqualTo("user_t");

        // OR
        Member findMember2 = queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq("user_t"),
                        member.age.eq(20)
                )
                .fetchOne();
        assertThat(findMember2.getName()).isEqualTo("user_t");

    }

    @Test
    public void resultFetch()
    {
//        Member fetchOne = queryFactory.selectFrom(QMember.member).fetchOne();
//        Member fetchFirst = queryFactory.selectFrom(QMember.member).fetchFirst();
        QueryResults<Member> results = queryFactory.selectFrom(member).fetchResults();
        results.getTotal();
        List<Member> content = results.getResults();

        long total = queryFactory
                .selectFrom(member)
                .fetchCount();
        System.out.println("total:"+total);
    }

    @Test
    public void sort()
    {
        em.persist(new Member("alpha",null,null,20));
        em.persist(new Member("beta",null,null,30));
        em.persist(new Member("go",null,null,40));

        List<Member> result = queryFactory.selectFrom(member)
                                            .where(member.age.goe(20))
                                            .orderBy(member.name.asc(), member.age.asc())
                                            .fetch();
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member member7 = result.get(2);

        assertThat(member5.getName()).isEqualTo("alpha");
        assertThat(member6.getName()).isEqualTo("beta");
        assertThat(member7.getName()).isEqualTo("go");

    }

    /**
     * List<Member> result = queryFactory
     *  .selectFrom(member)
     *  .orderBy(member.username.desc())
     *  .offset(1) //0부터 시작(zero index)
     *  .limit(2) //최대 2건 조회
     *  .fetch();
     *  assertThat(result.size()).isEqualTo(2);
     */
    @Test
    public void paging001()
    {


        List<Member> result = queryFactory.selectFrom(member)
                .orderBy(member.name.asc())
                .offset(1)//0 부터 시작
                .limit(10)//최대 20개 조회
                .fetch();
        System.out.println("result >>> : " + result.toString());
        assertThat(result.size()).isEqualTo(10);

        List<Member> result2 = queryFactory.selectFrom(member)
                .orderBy(member.name.asc())
                .offset(11)//11 부터 시작
                .limit(10)//최대 10개 조회
                .fetch();
        System.out.println("result2 >>> : " + result2.toString());


//        QTeam teamA = QTeam.team;
//        Team findTeam = queryFactory.select(teamA).from(teamA).where(teamA.name.eq("teamA")).fetchOne();
//        System.out.println("teamA:"+findTeam.toString());
//        System.out.println("teamA members:"+findTeam.getMembers().toString());

        assertThat(result2.size()).isEqualTo(10);
    }


    /**
     * 전체 조회 수가 필요하면?
     *
     */
    @Test
    public void paging2()
    {
        for(int i=0;i<26;i++)
        {
            Member member = new Member("user_"+(char)(97+i),null,null,i+1);
            em.persist(member);
        }

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .orderBy(member.name.desc())
                .offset(1)
                .limit(2)
                .fetchResults();// 전체 조회
        // 뭔가 위 쿼리에 where 로 이것저것 붙으면, 성능이 딸린다. 그럴 땐  count 만 따로 별도로 하는 게 좋을 수도 있다.
        System.out.println("results getTotal : "+results.getTotal());
        System.out.println("results getLimit : "+results.getLimit());
        System.out.println("results getOffset : "+results.getOffset());
        System.out.println("results getResults : "+results.getResults());
    }

    //Group By, Having

    /**
     * 실무에서는 잘 사용 x -> later, dto 에서 직접 뽑는 방법 있음요.
     */
    @Test
    public void agregation()
    {
        List<Tuple> result = queryFactory.select(member.count(), member.age.sum(), member.age.avg(), member.age.max(), member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
//        System.out.println("result:"+result.toString());//result:[[26, 351, 13.5, 26, 1]]result:[[26, 351, 13.5, 26, 1]]
        assertThat(tuple.get(member.count())).isEqualTo(30);
        assertThat(tuple.get(member.age.sum())).isEqualTo(451);
        assertThat(tuple.get(member.age.avg())).isEqualTo(15.033333333333333);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(1);
    }

    /**
     * 팀 이름과 각 팀의 평균
     * @throws Exception
     */
    @Test
    public void group() throws Exception
    {
        List<Tuple> results = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        System.out.println("results >>> "+results.toString());
//      [teamA, 15.0], [teamB, 35.0], [teamG, 13.0], [teamH, 14.0]][teamA, 15.0], [teamB, 35.0], [teamG, 13.0], [teamH, 14.0]]
        Tuple teamA = results.get(0);
        Tuple teamB = results.get(1);
        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15.0);

    }

    /**
     * 팀 A에 소속된 모든 회원
     * 조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭(alias)으로 사용할
     * Q 타입을 지정하면 된다.
     * - join() , innerJoin() : 내부 조인(inner join)
     * - leftJoin() : left 외부 조인(left outer join)
     * - rightJoin() : rigth 외부 조인(rigth outer join)
     * - JPQL의 on 과 성능 최적화를 위한 fetch 조인 제공 다음 on 절에서 설명
     *
     * join(조인 대상, 별칭으로 사용할 Q타입)
     */
    @Test
    public void join() throws Exception {
        QMember member = QMember.member;
        QTeam team = QTeam.team;
        System.out.println("===================query start===========");
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamH"))
                .fetch();
        System.out.println("===================query end===========");
        System.out.println("result : "+result.toString());
    }

    /**
     * ceta 조인, 그냥 알기만 하자 (막조인)
     * a.k.a -> cross join
     */
    @Test
    public void thetaJoin()
    {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        System.out.println("============= query start ==========");
        List<Member> result = queryFactory.select(member).from(member, team).where(member.name.eq(team.name)).fetch();
        System.out.println("============= query end  ===========");
        System.out.println("result : " + result);
        assertThat(result).extracting("name")
                .containsExactly("teamA","teamB");
    }

    /**
     * on 절을 활용한 조인(jpa 2.1부터 지원)
     * 1. 조인 대상 필터링
     * 2. 연관관계 없는 엔티티 외부 조인
     *
     * 1.조인 대상 필터링
     * 예] 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'teamH'
     * SQL:  SELECT m.*, t.* FROM Member m LEFT JOIN team t on m.team_id= t.TEAM_ID and t.name = 'teamH'
     * 참고: on 절을 활용해 조인 대상을 필터링 할 때, 외부조인이 아니라 내부조인(inner join)을 사용하면,
     * where 절에서 필터링 하는 것과 기능이 동일하다. 따라서 on 절을 활용한 조인 대상 필터링을 사용할 때,
     * 내부조인 이면 익숙한 where 절로 해결하고, 정말 외부조인이 필요한 경우에만 이 기능을 사용하자.
     */
    @Test
    public void joinOnFiltering()
    {
        List<Tuple> result = queryFactory.select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamH"))
                .fetch();

        for(Tuple tuple:result)
        {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 2. 연관관계 없는 엔티티 외부 조인
     * 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     * JPQL: SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
     * 주의! 문법을 잘 봐야 한다. leftJoin() 부분에 일반 조인과 다르게 엔티티 하나만 들어간다.
     * 일반조인: leftJoin(member.team, team)
     * on조인: from(member).leftJoin(team).on(xxx)
     */
    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamG"));
        em.persist(new Member("teamH"));
        System.out.println("======== query start ============");

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.name.eq(team.name))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("t=" + tuple);
        }
        System.out.println("======== query end   ============");
    }

    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();
        System.out.println("======== query start ============");
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.name.eq("user_a"))
                .fetchOne();
        boolean loaded =
                emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        System.out.println("======== query end   ============");
        assertThat(loaded).as("페치 조인 미적용").isFalse();

    }

    /**
     * 사용방법
     * join(), leftJoin() 등 조인 기능 뒤에 fetchJoin() 이라고 추가하면 된다.
     * @throws Exception
     */
    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();
        System.out.println("======== query start ============");
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.name.eq("user_a"))
                .fetchOne();
        System.out.println("======== query end   ============");
        boolean loaded =
                emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();

    }


    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");
        System.out.println("======== query start ============");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();
        System.out.println("======== query end   ============");
        System.out.println("result:"+result.toString());
        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 평균 나이 이상인 회원
     */
    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");
        System.out.println("======== query start ============");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        System.out.println("======== query end   ============");
        System.out.println("result:"+result.toString());
        assertThat(result).extracting("age")
                .containsExactly(25,24);
    }


    /**
     * 서브쿼리 여러 건 처리, in 사용
     *
     */
    @Test
    public void subQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");
        System.out.println("======== query start ============");
//        select member0_.member_id as member_i1_4_, member0_.address as address2_4_,
//        member0_.street as street3_4_, member0_.zipcode as zipcode4_4_,
//        member0_.age as age5_4_, member0_.name as name6_4_,
//        member0_.team_id as team_id7_4_
//        from member member0_
//        where member0_.age
//        in (select member1_.age from member member1_ where member1_.age>10);
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();
        System.out.println("======== query end   ============");
        System.out.println("result:"+result.toString());
//        assertThat(result).extracting("age").containsExactly(25, 24);
    }

    @Test
    public void subQueryInSelectClause() throws Exception
    {
        /**
         *   select
         *         member0_.name as col_0_0_,
         *         (select
         *             avg(cast(member1_.age as double))
         *         from
         *             member member1_) as col_1_0_
         *     from
         *         member member0_
         */
        QMember memberSub = new QMember("memberSub");
        List<Tuple> fetch = queryFactory
                .select(member.name,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ).from(member)
                .fetch();

        System.out.println("result : " + fetch.toString());

        for (Tuple tuple : fetch) {
            System.out.println("username = " + tuple.get(member.name));
            System.out.println("age = " +
                    tuple.get(JPAExpressions.select(memberSub.age.avg())
                            .from(memberSub)));
        }
    }

    @Test
    public void subQueryInSelectClauseUsingStaticImport() throws Exception
    {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        System.out.println("result : "+result.toString());
    }

    /** case 문 **/
    /** select, 조건절(where)에서 사용 가능 **/
    @Test
    public void simpleCaseClause() throws Exception
    {
        /**
         * select case when member0_.age between 0 and 20 then '0~20살' when member0_.age between 21 and 30 then '21~30살' else '기타' end as col_0_0_ from member member0_;
         */
        System.out.println("======== query start ============");
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        System.out.println("======== query end   ============");
        System.out.println("result:"+result.toString());
    }

    @Test
    public void littlMoreComplicatedCaseClause() throws Exception
    {
        System.out.println("======== query start ============");
        List<Tuple> result = queryFactory
                .select(member.id, member.name, new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        System.out.println("======== query end   ============");
        System.out.println("result:"+result.toString());
    }

    /** 상수, 문자 더하기 **/
    /**
     * 상수 더하기
     * 상수가 필요하면 Expressions.constant(xxx) 사용
     * > 참고: 위와 같이 최적화가 가능하면 SQL에 constant 값을 넘기지 않는다. 상수를 더하는 것 처럼 최적화가
     * 어려우면 SQL에 constant 값을 넘긴다.
     * @throws Exception
     */
    @Test
    public void stringConcat()throws Exception
    {
        Tuple result = queryFactory
                .select(member.name, Expressions.constant("A"))
                .from(member)
                .fetchFirst();
        System.out.println("result : "+result.toString());
    }
    /** 문자 더하기 concat **/

    /**
     * 문장려 더하기
     * > 참고: member.age.stringValue() 부분이 중요한데, 문자가 아닌 다른 타입들은 stringValue() 로 문
     * 자로 변환할 수 있다. 이 방법은 ENUM을 처리할 때도 자주 사용한다.
     * @throws Exception
     */
    @Test
    public void stringConcat2() throws Exception
    {
        String result = queryFactory
                .select(member.name.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.name.eq("member1"))
                .fetchOne();
        System.out.println("result:"+result.toString());
    }

    /**
     * 프로젝션과 결과 반환
     * 프로젝션: select 대상 지정
     */
    @Test
    public void f1()
    {
        List<String> result = queryFactory
                .select(member.name)
                .from(member)
                .fetch();
        System.out.println("result:"+result.toString());
    }

    /**
     * 튜플 조회
     * 프로젝션 대상이 둘 이상일 때 사용
     */
    @Test
    public void f2()
    {
        List<Tuple> result = queryFactory
                .select(member.name, member.age)
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            String username = tuple.get(member.name);
            Integer age = tuple.get(member.age);
            System.out.println("username=" + username);
            System.out.println("age=" + age);
        }
    }

    /**
     * 프로퍼티 접근 - Setter
     */
    @Test
    public void f3()
    {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.name,
                        member.age))
                .from(member)
                .fetch();
        System.out.println("result:"+result.toString());
    }

    /**
     * 필드 직접 접근
     */
    @Test
    public void f4()
    {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.name,
                        member.age))
                .from(member)
                .fetch();
        System.out.println("result:"+result.toString());
    }

    /**
     * 별칭이 다를 때
     * TODO 이건 강의 들을 때 빡 집중해서 보자
     */
    @Test
    public void f5()
    {
//        QMember memberSub = new QMember("memberSub");
//        List<UserDto> result = queryFactory
//                .select(Projections.fields(UserDto.class,
//                        member.name.as("name"),
//                        ExpressionUtils.as(
//                                JPAExpressions
//                                        .select(memberSub.age.max())
//                                        .from(memberSub), "age")
//                        )
//                ).from(member)
//                .fetch();
//        System.out.println("result:"+result.toString());

//        List<MemberDto> result = queryFactory
//                .select(Projections.constructor(MemberDto.class,
//                        member.name,
//                        member.age))
//                .from(member)
//                .fetch();
    }


    /**
     * QueryProjection 활용
     * 이 방법은 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법이다. 다만 DTO에 QueryDSL 어노테
     * 이션을 유지해야 하는 점과 DTO까지 Q 파일을 생성해야 하는 단점이 있다.
     * 참고 : distinct는 JPQL의 distinct와 같다.
     */
    @Test
    public void f6()
    {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.name, member.age))
                .from(member)
                .fetch();
        System.out.println("result:"+result.toString());

        List<String> result2 = queryFactory
                .select(member.name).distinct()
                .from(member)
                .fetch();
        System.out.println("result2:"+result2.toString());
    }






}
