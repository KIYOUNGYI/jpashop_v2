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
    public void basicQuerydslTest()
    {
        Member member = new Member("memberdummy001");
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);
        List<Member> result1 = memberRepository.findAll();

        System.out.println("result 1 : "+result1.toString());
        assertThat(result1).contains(member);
    }

    @Test
    public void searchTest()
    {
        Member member = new Member("memberdummy001");
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);
        List<Member> result1 = memberRepository.findAll();

        System.out.println("result 1 : "+result1.toString());
        assertThat(result1).contains(member);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(40);
        condition.setAgeLoe(50);
        condition.setTeamName("teamY");

        List<MemberTeamDto> result2 = memberRepository.search(condition);
        System.out.println("result2:"+result2.toString());

        assertThat(result2).extracting("username").contains("d_member_47");

    }
}
