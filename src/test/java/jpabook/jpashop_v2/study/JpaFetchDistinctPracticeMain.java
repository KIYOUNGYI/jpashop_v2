package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaFetchDistinctPracticeMain
{
    public static void main(String[] args)
    {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        System.out.println("JpaFetchDistinctPracticeMain");
        try
        {

            String query = "select distinct t FROM Team t join fetch t.members";

            List<Team> teams = em.createQuery(query, Team.class).getResultList();


            for(Team team : teams)
            {
                System.out.println("teamname = " + team.getName() + ", team = " + team);
                for (Member member : team.getMembers())
                {
                    //페치 조인으로 팀과 회원을 함께 조회해서 지연 로딩 발생 안함
                    System.out.println("-> username = " + member.getName()+ ", member = " + member);
                }
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
