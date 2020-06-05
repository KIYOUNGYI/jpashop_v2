package jpabook.jpashop_v2.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.QMember;
import jpabook.jpashop_v2.domain.QTeam;
import jpabook.jpashop_v2.domain.Team;
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
import javax.transaction.Transactional;

import java.util.List;

import static jpabook.jpashop_v2.domain.QMember.*;
import static jpabook.jpashop_v2.domain.QMember.member;
import static jpabook.jpashop_v2.domain.QTeam.team;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional// EntitiyManager 활용하려면 이게 있어야 하네?
//@Commit
public class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

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
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamH"))
                .fetch();

        System.out.println("result : "+result.toString());
    }




}