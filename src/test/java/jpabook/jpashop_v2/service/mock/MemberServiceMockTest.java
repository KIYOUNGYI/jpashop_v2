package jpabook.jpashop_v2.service.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.repository.MemberJpaRepository;
import jpabook.jpashop_v2.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceMockTest {

//  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberJpaRepository memberRepository;

  @BeforeEach
  void setup() {
    memberService = new MemberService(memberRepository);
  }

  @Test
  @DisplayName("mockito 레포지토리 테스트")
  void mockMemberRepositoryTest() {
    //given
    Member member1 = Member.builder().name("mock1").age(20).build();
    List<Member> members = new ArrayList<>();
    members.add(member1);

    given(memberRepository.findAll()).willReturn(members);
    //when
    List<Member> findMembers = memberRepository.findAll();
    System.out.println("findMembers = " + findMembers);

    //then
    assertEquals(1, findMembers.size());
    assertEquals(member1.getName(), findMembers.get(0).getName());
  }

  @Test
  @DisplayName("mockito service 테스트")
  void mockMemberServiceTest() {

    //given
    Member member1 = Member.builder()
        .name("mock유저1")
        .age(20)
        .build();
    List<Member> members = new ArrayList<>();
    members.add(member1);

    given(memberRepository.findAll()).willReturn(members);

    //when
    List<Member> findMembers = memberService.findAll();

    System.out.println("memberAll = " + findMembers);

    //then
    assertEquals(member1.getName(), findMembers.get(0).getName());
  }


}
