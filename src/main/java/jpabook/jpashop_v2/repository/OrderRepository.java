package jpabook.jpashop_v2.repository;

import jpabook.jpashop_v2.domain.Order;
import jpabook.jpashop_v2.domain.OrderSearch;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository
{
    private final EntityManager em;

    public void save(Order order)
    {
        try
        {
            em.persist(order);
        }
        catch (Exception e)
        {
            System.out.println("error ? "+ e.toString());
        }

    }

    public Order findOne(Long id)
    {
        return em.find(Order.class,id);
    }

//    public List<Order> findAll(OrderSearch orderSearch)
//    {
//        //querydsl 쓰자
//    }

}
