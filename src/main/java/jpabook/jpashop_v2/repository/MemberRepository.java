package jpabook.jpashop_v2.repository;

import jpabook.jpashop_v2.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository
{
//    [방법1]
//    @PersistenceContext @Autowired
//    private EntityManager em;
//    //스프링이 em을 만들어서 주입해줌
//
//    [방법2]
    private final EntityManager em;

    // 원래는 persistencecontext 가 어노테이션 표준인데, 스프링부트(datajpa)가 autowired 도 지원해줌

    public void save(Member m)
    {
        em.persist(m);
    }

    public Member findOne(Long id)
    {
        return em.find(Member.class,id);
    }

    public List<Member> findAll()
    {
        // JPQL
        // sql은 테이블을 대상으로 퀘리를 날리는데, JPQL은 객체를 대상으로 쿼리를 날림
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name)
    {
        return em.createQuery("select m " +
                                       "from Member m " +
                                       "where m.name = :name", Member.class)
                .setParameter("name",name)//parameter binding
                .getResultList();
    }
}
