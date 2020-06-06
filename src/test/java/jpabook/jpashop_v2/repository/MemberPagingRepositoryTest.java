package jpabook.jpashop_v2.repository;


import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.dto.MemberSearchCondition;
import jpabook.jpashop_v2.dto.MemberTeamDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberPagingRepositoryTest
{
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;



    @Test
    public void searchTest()
    {
        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        PageRequest pageRequest = PageRequest.of(0,20);

        Page<MemberTeamDto> result = memberRepository.searchPageSimple(memberSearchCondition, pageRequest);
        System.out.println("result : " + result.toString());
        System.out.println("result getcontents : "+ result.getContent());
        System.out.println("size : "+result.getSize());
        System.out.println("total pages : "+result.getTotalPages());
        Assertions.assertThat(result.getContent()).extracting("username").contains("member1");
    }
}
