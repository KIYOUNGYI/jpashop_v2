package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaStudyUpdateMain
{
    public static void main(String[] args)
    {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        System.out.println("JpaStudyUpdateMain");
        try {
            Member member1 = em.find(Member.class,4193l);
            member1.setName("new_member_4193");
            member1.setAge(99);
//            em.update(member1);//이런 코드가 있어야 하는거 아닌감???
            transaction.commit();
        } catch (Exception e) {
            System.out.println("error :" + e.toString());
            transaction.rollback();
        } finally {
            em.close();
        }
    }
}
