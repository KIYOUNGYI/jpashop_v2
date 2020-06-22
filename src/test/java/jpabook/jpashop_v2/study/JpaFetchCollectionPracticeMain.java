package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaFetchCollectionPracticeMain
{
    public static void main(String[] args)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        System.out.println("JpaFetchCollectionPracticeMain");
        try
        {
//            Team teamX = new Team("teamX");
//            Team teamY = new Team("teamY");
//            em.persist(teamY);
//            em.persist(teamX);
//            Member member1 = new Member("memberX1999");
//            member1.setTeam(teamX);
//            Member member2 = new Member("memberX1998");
//            member2.setTeam(teamX);
//            Member member3 = new Member("memberX1997");
//            member3.setTeam(teamY);
//            em.persist(member1);
//            em.persist(member2);
//            em.persist(member3);
//            em.flush();//db에 꽂고
//            em.clear();//영속성컨텍스트 비워주고

            String query = "select t FROM Team t join fetch t.members";

            List<Team> teams = em.createQuery(query, Team.class).getResultList();

//            for(Team team: teams)
//            {
//                System.out.println("team = " + team.getName()+" | members = " + team.getMembers().size());
//            }
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

/**
 select
 team0_.team_id as team_id1_9_0_,
 members1_.member_id as member_i1_4_1_,
 team0_.name as name2_9_0_,
 members1_.address as address2_4_1_,
 members1_.street as street3_4_1_,
 members1_.zipcode as zipcode4_4_1_,
 members1_.age as age5_4_1_,
 members1_.name as name6_4_1_,
 members1_.team_id as team_id7_4_1_,
 members1_.team_id as team_id7_4_0__,
 members1_.member_id as member_i1_4_0__
 from
 Team team0_
 inner join
 Member members1_
 on team0_.team_id=members1_.team_id
 **/