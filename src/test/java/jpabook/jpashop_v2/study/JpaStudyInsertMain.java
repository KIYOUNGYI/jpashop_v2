package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *  <property name="hibernate.jdbc.batch_size" value="10"/>
 *  한번에 10개 모아서 db에 쿼리
 */
public class JpaStudyInsertMain
{

    public static void main(String[] args)
    {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        System.out.println("JpaStudyInsertMain");
        try
        {
            Member member1 = new Member("member1999");
            Member member2 = new Member("member1998");
            em.persist(member1);
            em.persist(member2);
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
