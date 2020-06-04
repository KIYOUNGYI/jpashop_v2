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
class JpashopV2ApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	@Transactional // 테스트케이스에 있으면 바로 롤백 시켜버림
//	@Rollback(false)// 근데 가끔 데이터를 넣어서 눈으로 직접 확인하고 싶을땐, 이 옵션을 사용하면 된다.
	public void dummy()
	{
		System.out.println("are you here?");
		Member member = new Member();
		member.setName("dummy");
		em.persist(member);
		JPAQueryFactory query = new JPAQueryFactory(em);
		QMember qMember = new QMember("m");

		Member result = query.selectFrom(qMember).fetchOne();

		System.out.println("result:"+result);
		System.out.println("member:"+member);
		System.out.println("==============");
		System.out.println("result.getId:"+result.getId());
		System.out.println("member.getId:"+member.getId());

//		assertThat(result).isEqualTo(member);
//		assertThat(result.getId()).isEqualTo(member.getId());
	}

}
