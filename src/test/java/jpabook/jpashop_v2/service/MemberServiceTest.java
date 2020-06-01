package jpabook.jpashop_v2.service;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest
{

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    public void 회원가입() throws Exception
    {
        //given
        Member member = new Member();
        member.setName("Kim");
        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member,memberRepository.findOne(savedId));
    }

    @Test
    public void 중복회원예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("Kim");

        Member member2 = new Member();
        member2.setName("Kim");

//       plan A
//        assertThrows(IllegalStateException.class,
//                () ->
//                {
//                    //when
//                    memberService.join(member1);
//                    memberService.join(member2);
//                }
//        );
//        fail("여기까지 도달하면 안되는건데...");

        //plan B
        try
        {
            memberService.join(member1);
            memberService.join(member2);
        }
        catch (IllegalStateException e)
        {
            return;
        }
    }
//    스택오버플로우 예제 (keyword:junit5 how to assert an exception is thrown)
//    https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
//    @Test
//    public void itShouldThrowNullPointerExceptionWhenBlahBlah() {
//        assertThrows(NullPointerException.class,
//                ()->{
//                    //do whatever you want to do here
//                    //ex : objectName.thisMethodShoulThrowNullPointerExceptionForNullParameter(null);
//                });
//    }

}