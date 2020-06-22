package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaFetchManyToOnePracticeMain
{
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        System.out.println("JpaFetchManyToOnePracticeMain");
        try
        {
//            Team teamX = new Team("teamX");
//            Team teamY = new Team("teamY");
//            em.persist(teamY);
//            em.persist(teamX);
//
//
//            Member member1 = new Member("memberX1999");
//            member1.setTeam(teamX);
//            Member member2 = new Member("memberX1998");
//            member2.setTeam(teamX);
//
//            Member member3 = new Member("memberX1997");
//            member3.setTeam(teamY);
//
//            em.persist(member1);
//            em.persist(member2);
//            em.persist(member3);
//
//            em.flush();//db에 꽂고
//            em.clear();//영속성컨텍스트 비워주고


            // [basic]
//            String query = "select m FROM Member m";
//
//            List<Member> result = em.createQuery(query,Member.class).getResultList();
//
//            for(Member member: result)
//            {
//                System.out.println("member = " + member.getName()+" | " + member.getTeam().getName());
//            }
            // member.getTeam() <- 이녀석은 proxy 이고, 영속성 컨텍스트에 데이터 내놔 하는거죠.
            //회원 1 | 팀x (sql)
            //회원 2 | 팀x (캐시)
            //회원 3 | 팀y (sql)
            // n+1 problem
            // 회원 100명인데 팀 다 다르면, 101번 나간느거지, 이건 즉시로딩이든, 지연로딩이든 무조건 발생.

            String query = "select m FROM Member m join fetch m.team";

            List<Member> result = em.createQuery(query,Member.class).getResultList();

            for(Member member: result)
            {
                System.out.println("member = " + member.getName()+" | " + member.getTeam().getName());
            }
            transaction.commit();
        }
        catch (Exception e)
        {
            System.out.println("error :" +e.toString());
            transaction.rollback();
        }
        finally
        {
            em.close();
        }
    }
}
