package jpabook.jpashop_v2.service;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional//public method 는 기본적으로 먹고 들어감
@RequiredArgsConstructor
public class MemberService
{

//    @Autowired
//    private MemberRepository memberRepository;

    // 위의 방식은 단점 있음.

    //[1] setter Injection
    //장점: 테스트때 목 객체 주입할 때 좀 편함.
    //단점: 치명적 -> 런타임때, 누군가 이걸 바꾸면(?) 근데 정말 그런 경우가 있을지는
//    private MemberRepository memberRepository;
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository)
//    {
//        this.memberRepository = memberRepository;
//    }

    //[2] 생성자 injection

    private final MemberRepository memberRepository;
    // 장점1 : 한번 was 뜨면 생성자 생성될 때 한 번 만들어질 때, 제외하곤, 변경될 일이 없음
    // 장점2 : 테스트 할 때 멤버서비스를 작성할 때, 뭔가 이걸 주입해줘야해 그런거 인지시킬 수 있는 장점이 있는 것 같다.
    // 장점3 : 사실은 autowired 없어도 됨, 요즘 스프링 최신 버전들은 이걸 지원해줌
    // 권장 : final 추천, 컴파일 시점에 값 주입 안시켜주면 에러 뿜어냄 (might not have been initialized)

//    @Autowired
//    public MemberService(MemberRepository memberRepository)
//    {
//        this.memberRepository = memberRepository;
//    }
    // lombok allArgConstructor -> 아래걸 만들어줌
//    public MemberService(MemberRepository memberRepository)
//    {
//        this.memberRepository = memberRepository;
//    }
    // lombok requiredAgsConstructor -> final 키워드만 달린 멤버변수만 care함.(짱인듯)

    //회원가입
    //회원전체조회
    @Transactional
    public Long join(Member member)
    {
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }


    private void validateDuplicatedMember(Member member)
    {
        // 최후에 member 네임에 unique 제약을 걸어주는게 안전,
        // 동시에 2명 이상의 사람이 서로 다른 was 로 같은 시간에 호출할 수도 있다.
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(findMembers.size()>0)
        {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     * @return
     */
    @Transactional(readOnly = true)//성능 최적화
    public List<Member> findAll()
    {
        return memberRepository.findAll();
    }
    @Transactional(readOnly = true)//성능 최적화
    public Member findOne(Long id)
    {
        return memberRepository.findOne(id);
    }

    /**
     * member -> 영속 상태 / setName 이름 바꿔주면, 종료되면, 스프링 aop 가 동작하면서 트랜잭션 어노테이션에 의해서 트랜잭션 관련 aop 끝나는 시점에 커밋하고 jpa 가 플러시하고 database transaction 커밋하고
     * @param id
     * @param name
     */
    @Transactional
    public void update(Long id, String name)
    {
        Member member = memberRepository.findOne(id);// 트랜잭션(어노테이션) 있는 상태에서 가져오면, 영속성 컨텍스트에서 가지고 옴,
        member.setName(name);//바꾸면, 트랜잭션이 끝나고 commit 되는 시점에서 jpa가 변경감지 실행합니다.
    }
}
