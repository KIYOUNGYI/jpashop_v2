package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaStudyMain
{
    public static void main(String[] args)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        System.out.println("JpaStudyMain");
        try
        {
            Member findMember1 = em.find(Member.class,3l);
            Member findMember2 = em.find(Member.class, 3l);
            Member findMember3 = em.find(Member.class, 3l);
            System.out.println("findMember1 == findMember2 "+ (findMember1==findMember2));
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
