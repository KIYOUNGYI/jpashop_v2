package jpabook.jpashop_v2.study;

/**
 *
 * parent -> child
 * 쓸만한건 all, persist 정도
 * (일대다) 다 써야되나
 * 하나의 부모가 여러 자식을 관리할 때는 의미있음, ex> 게시판, 첨부파일 경로 의미가 있음
 * 쓰면 안되는 경우 : 파일을 여러 군데에서 관리를 할 경우는
 *
 * 다른 엔티티가 child 와 연관관계가 있을땐 뷁
 * 단일 엔티티에 완전히 종속적일 땐, 이걸 사용해도 괜찮당.
 * 1] 라이프사이클이 똑같을때 (패런트,차일드의)
 * 2] 단일소유자 parent가 child 라는 엔티티를 소유할 때,
 *
 * 고아객체
 * 부모엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
 * 참조하는 곳이 하나일 때 사용해야함! (주의합시다)
 */
public class CascadePractice {
}