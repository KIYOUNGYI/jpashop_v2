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

    public List<Order> findAll()
    {
        List<Order> temp = em.createQuery("select o from Order o",Order.class).getResultList();
//        System.out.println("temp size:"+temp.size());
        return temp;
    }

    /**
     * fetch 는 jpa 에만 있는 문법. sql에는 없지...
     * 실무에서 자주사용하는건데, 깊이가 생각이 깊음.
     * 이거 100% 이해를 해야함.
     *
     * 쿼리 1번에 다 가져오는 장점이 있다만, 단점은 굳이 뽑자면, 필요없는 필드도 많이 뽑아낸다 정도?
     * @return
     */
    //OrderRepository 추가 코드
    public List<Order> findAllWithMemberDelivery() {
        // 레이지 로딩 이런거 고민안해도 됨.
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class).getResultList();
    }
    /**
     *     select
     *         order0_.order_id as order_id1_7_0_,
     *         member1_.member_id as member_i1_4_1_,
     *         delivery2_.delivery_id as delivery1_2_2_,
     *         order0_.delivery_id as delivery4_7_0_,
     *         order0_.member_id as member_i5_7_0_,
     *         order0_.order_date as order_da2_7_0_,
     *         order0_.status as status3_7_0_,
     *         member1_.address as address2_4_1_,
     *         member1_.street as street3_4_1_,
     *         member1_.zipcode as zipcode4_4_1_,
     *         member1_.name as name5_4_1_,
     *         delivery2_.address as address2_2_2_,
     *         delivery2_.street as street3_2_2_,
     *         delivery2_.zipcode as zipcode4_2_2_,
     *         delivery2_.delivery_status as delivery5_2_2_
     *     from
     *         orders order0_
     *     inner join
     *         member member1_
     *             on order0_.member_id=member1_.member_id
     *     inner join
     *         delivery delivery2_
     *             on order0_.delivery_id=delivery2_.delivery_id
     */


}
