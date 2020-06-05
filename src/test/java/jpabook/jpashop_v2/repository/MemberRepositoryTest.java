package jpabook.jpashop_v2.repository;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;
import jpabook.jpashop_v2.dto.MemberSearchCondition;
import jpabook.jpashop_v2.dto.MemberTeamDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest
{
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void test001()
    {

    }

    @Test
    public void basicQuerydslTest() {
        Member member = new Member("memberdummy001");
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);
        List<Member> result1 = memberRepository.findAll_Querydsl();

        System.out.println("result 1 : "+result1.toString());
        assertThat(result1).contains(member);


        List<Member> result2 = memberRepository.findByUsername_Querydsl("memberdummy001");
        System.out.println("result 2 : "+result2.toString());

        assertThat(result2).containsExactly(member);
    }

    @Test
    public void searchTest() {
        Team teamA = new Team("teamX");
        Team teamB = new Team("teamY");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("d_member1", null, teamA,10);
        Member member2 = new Member("d_member2", null, teamA,20);
        Member member3 = new Member("d_member3", null, teamB,30);
        Member member4 = new Member("d_member4", null, teamB,40);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamY");
        List<MemberTeamDto> result =
                memberRepository.searchByBuilder(condition);
        System.out.println("result:"+result.toString());
        assertThat(result).extracting("username").containsExactly("d_member4");

        List<MemberTeamDto> result2 =
                memberRepository.search(condition);
        System.out.println("result:"+result2.toString());
        assertThat(result2).extracting("username").containsExactly("d_member4");

    }
}
