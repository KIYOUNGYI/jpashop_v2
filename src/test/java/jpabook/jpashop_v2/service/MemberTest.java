package jpabook.jpashop_v2.service;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
//@Commit
public class MemberTest
{
    @PersistenceContext
    EntityManager em;
    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", null, teamA ,10);
        Member member2 = new Member("member2", null, teamA, 20);
        Member member3 = new Member("member3", null, teamB, 30);
        Member member4 = new Member("member4", null, teamB, 40);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        //초기화
        em.flush();
        em.clear();
        //확인
        List<Member> members = em.createQuery("select m from Member m",
                Member.class)
                .getResultList();
        for (Member member : members) {
            System.out.println("member=" + member);
            System.out.println("-> member.team=" + member.getTeam());
        }
    }
}

