package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

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
            Member member = new Member();
            member.setName("Paul Yi");
            member.setAge(31);
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

            Member member1 = new Member();
            member1.setName("Paul Yi");
            member1.setAge(31);
            member1.setTeam(team);
            Member member2 = new Member();
            member2.setName("Kyle Yi");
            member2.setAge(32);
            member2.setTeam(team);


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

            Member member1 = new Member();
            member1.setName("Paul Yi");
            member1.setAge(31);
            member1.setTeam(team);
            Member member2 = new Member();
            member2.setName("Kyle Yi");
            member2.setAge(32);
            member2.setTeam(team);


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
}
