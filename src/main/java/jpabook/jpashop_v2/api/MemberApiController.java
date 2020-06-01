package jpabook.jpashop_v2.api;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 강조: 엔티티를 절대 노출시키지 말고, api 스펙에 맞는 dto를 만들어주세요 <- 장애를 방지하는 마지노선
 * request,response dto 내부에 inner로 만드는거 좋아보임.
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController
{

    private final MemberService memberService;

    /**
     * 심각한 문제
     * [1] notEmpty 를 모델에서 체크하는건 나중에 문제가 있을 수 있음.
     * [2] member entity -> name 에서 username 으로 바꾸면, api 스펙이 바뀜. 이게 문제, 클라이언트 팀에서 호출하면 어쩔
     * [3] 궁극적으로 큰 장애가 많이 발생하게 됨. -> dto를 만드는게 맞음
     * [4] 외부에 entity를 노출시키지 말고, 파라미터로 받지도 말고,
     * @param member
     * @return
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member)
    {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * [1] api 스펙이 바뀌면 알아차리지 -> 빨강색 컴파일 에러
     * [2] 엔티티만 보고는 어떤 값이 오는지 각이 안나옴, 그런데, dto를 보면, 딱 name 만 보면, 된다. 이말이지 // dto 에서 notEmpty 등의 validation 체크를 하면 된다.
     * [3] api 스펙에 맞는 dto 만드는게 좋음
     * @param request
     * @return
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request)
    {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * command,query 분리해서 코딩하는것도 좋은 습관인것 같다.
     * memberService.update 리턴값 없는 스탈
     * @param id
     * @param request
     * @return
     */
    @PutMapping("api/v2/members/{id}")
    public updateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request)
    {
        //변경감지
        memberService.update(id,request.getName());
        Member findMember = memberService.findOne(id);;
        return new updateMemberResponse(findMember.getId(),findMember.getName());
    }


    /**
     * spring 기본적으로 jackson을 사용함.
     * order 필요없으면 @JsonIgnore 넣어주면 되긴 하는데, order 정보 필요로 하면 어쩔껴??
     * 그리고 엔티티에 프레센테이션 계층을 위한 로직이 들어가는 순간, 거지같아지는거임
     * 결정적으로 Member 의 필드변수 name이 userName으로 변경되는 순간? 대 참사가 발생하는 것임 -> api 스펙이 변경되는 것 -> 장애
     * @return
     */
    @GetMapping("api/v1/members")
    public List<Member> membersV1()
    {
        return memberService.findAll();
    }

    @GetMapping("api/v2/members")
    public Result membersV2()
    {
        List<Member> findMembers = memberService.findAll();

        List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getId(), m.getName())).collect(Collectors.toList());
//        return new Result(collect);
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>
    {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto
    {
        private Long user_id;
//        private Long id;
//        private String name;
        private String user_name;//이게 키값
    }

    @Data
    @AllArgsConstructor
    static class updateMemberResponse
    {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest
    {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest
    {
        private String name;
    }
    @Data
    static class CreateMemberResponse
    {
        private Long id;
        public CreateMemberResponse(Long id)
        {
            this.id = id;
        }
    }
}
