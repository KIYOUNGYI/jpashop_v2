//package jpabook.jpashop_v2.service;
//
//import jpabook.jpashop_v2.domain.Member;
//import jpabook.jpashop_v2.domain.Team;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//@SpringBootTest
//@Transactional
////@Commit
//public class QuerydslBasicTest
//{
//    @Autowired
//    EntityManager em;
//
//    @Test
//    public void go() {
//        Team teamE = new Team("teamE");
//        Team teamF = new Team("teamF");
//        em.persist(teamE);
//        em.persist(teamF);
//        Member member1 = new Member("member5", null, teamE,30);
//        Member member2 = new Member("member6", null, teamE,40);
//        Member member3 = new Member("member7", null, teamF,50);
//        Member member4 = new Member("member8", null, teamF,30);
//        em.persist(member1);
//        em.persist(member2);
//        em.persist(member3);
//        em.persist(member4);
//    }
//    @Test
//    public void di()
//    {
//
//    }
//}
//
