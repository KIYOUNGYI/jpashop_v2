package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;
import org.hibernate.Hibernate;
import org.hibernate.jpa.internal.PersistenceUnitUtilImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@SpringBootTest
@Transactional
//@Rollback(false)
public class ProxyPractice
{
    @Autowired
    EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void f2()
    {
        Member member = em.find(Member.class,9L);
        printMemberAndTeam(member);
    }

    private void printMemberAndTeam(Member member)
    {
        String userName = member.getName();
        System.out.println("userName:"+userName);

        Team team = member.getTeam();
        System.out.println("team:"+team.getName());
    }


    @Test
    public void f1()
    {
        System.out.println("dude!!!!");
        try
        {
            Team team = new Team("alpha");
            Member member = new Member("Paul Yi",31,team);
            em.persist(team);
            em.persist(member);
            em.flush();
            em.clear();

            Member findMember  = em.getReference(Member.class,member.getId());
            System.out.println(">>>>>>>> before : "+ findMember.getClass());
            System.out.println(">>>>>>>> findMember.getId() : "+ findMember.getId());
            System.out.println(">>>>>>>> findMember.getUserName() : "+ findMember.getName());
            System.out.println(">>>>>>>> after : "+ findMember.getClass());
        }
        catch (Exception e)
        {
        }
        finally
        {

        }
    }

    @Test
    public void f3()
    {
        try
        {
            Team team = new Team("alpha");
            em.persist(team);

            Member member1 = new Member("Paul Yi",31,team);
            Member member2 = new Member("Kyle Yi",32,team);

            em.persist(member1);
            em.persist(member2);
            em.flush();
            em.clear();

            Member m1  = em.find(Member.class,member1.getId());
            Member m2 = em.getReference(Member.class,member2.getId());

            System.out.println("=== m1 : "+ m1.getClass());//member
            System.out.println("=== m2 : "+ m2.getClass());//proxy
            //m2 프록시로 반환해봐야 얻는 이점이 없다.

            logic(m1,m2);


        }
        catch (Exception e)
        {
            System.out.println("error!!!!");
            e.printStackTrace();
        }
        finally
        {

        }
    }

    private void logic(Member m1, Member m2)
    {
        System.out.println("m1==m2"+ (m1.getClass()==m2.getClass()));//f3 -> false
        System.out.println("m1 :" + (m1 instanceof Member));//타입체크시 true
        System.out.println("m2 :" + (m2 instanceof Member));//타입체크시 true
    }

    @Test
    public void f4()
    {
        try
        {
            Team team = new Team("alpha");
            em.persist(team);

            Member member1 = new Member("Paul Yi",31,team);

            Member member2 = new Member("Kyle Yi",32,team);
            em.persist(member1);
            em.persist(member2);
            em.flush();
            em.clear();

            Member refMember  = em.getReference(Member.class,member1.getId());
            Member findMember = em.find(Member.class,member1.getId());

            System.out.println("=== m1 : "+ refMember.getClass());
            System.out.println("=== m2 : "+ findMember.getClass());
            //m2 프록시로 반환해봐야 얻는 이점이 없다.
            System.out.println("m1==m2"+ (refMember.getClass()==findMember.getClass()));//jpa 에서는 이걸 어떻게든 맞춰준다.
//            logic(m1,m2);


        }
        catch (Exception e)
        {
            System.out.println("error!!!!");
            e.printStackTrace();
        }
        finally
        {

        }
    }


    @Test
    public void f5()
    {
        try
        {
            Team team = new Team("alpha");
            em.persist(team);

            Member member1 = new Member("Paul Yi",31,team);
            Member member2 = new Member("Kyle Yi",32,team);

            em.persist(member1);
            em.persist(member2);
            em.flush();
            em.clear();

            Member refMember  = em.getReference(Member.class,member1.getId());
            System.out.println("refMember="+refMember.getClass());
//            refMember.getName();//이런 코드가 있으면, 강제로 초기화됨
            Hibernate.initialize(refMember);//hibernate 가 호출하는 강제초기화 코드

            System.out.println("isLoaded="+emf.getPersistenceUnitUtil().isLoaded(refMember));



        }
        catch (Exception e)
        {
            System.out.println("error!!!!");
            e.printStackTrace();
        }
        finally
        {

        }
    }


    @Test
    public void 지연로딩()
    {
        try
        {
            Team team = new Team("alpha");
            em.persist(team);

            Member member1 = new Member("Paul Yi",31,team);
            em.persist(member1);
            em.flush();
            em.clear();

            Member m = em.find(Member.class,member1.getId());

            System.out.println("m = "+ m.getTeam().getClass());
            // @ManyToOne(fetch = FetchType.LAZY)
            //    @JoinColumn(name="team_id")
            //    private Team team;
            // m = class jpabook.jpashop_v2.domain.Team$HibernateProxy$9IQog6qH
            System.out.println("============================");
            m.getTeam().getName();//터치하면 이 시점에 쿼리가 나간다.
        }
        catch (Exception e)
        {
            System.out.println("error!!!!");
            e.printStackTrace();
        }
        finally
        {

        }
    }


    @Test
    public void 즉시로딩()
    {
        try
        {
            Team team = new Team("alpha");
            em.persist(team);

            Member member1 = new Member("Paul Yi",31,team);
            Member member2 = new Member("Kyle Yi",32,team);
            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            Member m = em.find(Member.class,member1.getId());

            System.out.println("m = "+ m.getTeam().getClass());

            List<Member> select_m_from_member_m = em.createQuery("select m from Member m", Member.class).getResultList();

            // 제일 처음 하는 것은 sql 로 번역
            // SELECT * FROM MEMBER
            // MEMBER 를 가져왔더니 어라? 팀이 즉시로딩으로 가져오라 하네?
            // 그러면, 즉시로딩이란 말은 무조건 값이 다 들어가 있어야 되용.
            // select * from team where team_id = member.team_id blah blah blah
            // 팀의 개수만큼 쿼리가 나간다.
            System.out.println("select_m_from_member_m:"+select_m_from_member_m.toString());


            System.out.println("============================");
            m.getTeam().getName();//터치하면 이 시점에 쿼리가 나간다.
        }
        catch (Exception e)
        {
            System.out.println("error!!!!");
            e.printStackTrace();
        }
        finally
        {

        }
    }


    @Test
    public void 페치조인()
    {
        try
        {
            Team team = new Team("alpha");
            em.persist(team);

            Member member1 = new Member("Paul Yi",31,team);
            Member member2 = new Member("Kyle Yi",32,team);
            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            Member m = em.find(Member.class,member1.getId());

            System.out.println("m = "+ m.getTeam().getClass());

            List<Member> select_m_from_member_m = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

            System.out.println("select_m_from_member_m:"+select_m_from_member_m.toString());


            System.out.println("============================");
            m.getTeam().getName();
            Member m2 = em.find(Member.class,member2.getId());
            System.out.println("22222222====================");
            m2.getTeam().getName();
        }
        catch (Exception e)
        {
            System.out.println("error!!!!");
            e.printStackTrace();
        }
        finally
        {

        }
    }
}
