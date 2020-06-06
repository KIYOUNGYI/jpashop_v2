package jpabook.jpashop_v2.repository;

import jpabook.jpashop_v2.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom
{

}
