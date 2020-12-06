package jpabook.jpashop_v2.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.repository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest//스프링 부트 띄운 상태로 테스트 돌리는 녀석, 이게 없으면 오토와이어드 안 먹는다.(밑에)
@Transactional
class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberJpaRepository memberRepository;
  @Autowired
  EntityManager em;

  //   * rollback(false) 가 없으면 아이에 insert query 가 나가지 않습니다.
  //   * 왜냐하면 @Transactional 기본이 롤백 시키는 것이기 때문입니다.
  //   * jpa 입장에서는 insert query를 db에 날릴 이유가 없죠. 정확하게 애기하면, 영속성 컨텍스트를 플러시 시키지 않는거죠.
  //    EntityManager em; em.flush() 하면 쿼리 나가는 것을 볼 수는 있음. 다만 실제 트랜잭션은 롤백 시킴.

  @Test
  public void 회원가입() throws Exception {
    //given
    Member member = new Member("Kim");
    //when
    Long savedId = memberService.join(member);

    //then
//    em.flush();//디비에 쿼리 강제로 날라가는 거임.
    assertEquals(member, memberRepository.findOne(savedId));
  }

  @Test
  public void 중복회원예외001() throws Exception {
    //given
    Member member1 = new Member("Kim");

    Member member2 = new Member("Kim");

    assertThrows(IllegalStateException.class, () ->
        {
          //when
          memberService.join(member1);
          memberService.join(member2);
        }
    );

  }

  @Test
  public void 중복회원예외002() throws Exception {
    //given
    Member member1 = new Member("Kim");

    Member member2 = new Member("Kim");


    memberService.join(member1);
    try {
      memberService.join(member2);
    } catch (IllegalStateException e) {
      e.printStackTrace();
      return;
    }
    fail("여기까지 도달하면 안되는건데...");
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