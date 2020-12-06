package jpabook.jpashop_v2;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.QMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트케이스에 있으면 바로 롤백 시켜버림
class JpashopV2ApplicationTests {

	@Autowired
	EntityManager em;

	@Test
//	@Rollback(false)// 근데 가끔 데이터를 넣어서 눈으로 직접 확인하고 싶을땐, 이 옵션을 사용하면 된다.
	public void dummy()
	{
		Member member = new Member("hello_liki");
		em.persist(member);
		JPAQueryFactory query = new JPAQueryFactory(em);
		QMember qMember = new QMember("m");

		Member result = query.selectFrom(qMember).where(qMember.name.eq("hello_liki")).fetchOne();

		System.out.println("result:"+result.toString());

		Assertions.assertThat(result).isEqualTo(member);
		Assertions.assertThat(result.getId()).isEqualTo(member.getId());
	}

}
