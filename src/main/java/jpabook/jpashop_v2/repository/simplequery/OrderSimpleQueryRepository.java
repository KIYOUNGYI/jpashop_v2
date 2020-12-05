package jpabook.jpashop_v2.repository.simplequery;

import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpashop_v2.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

  private final EntityManager em;

  public List<OrderSimpleQueryDto> findOrderDtos() {
    return em.createQuery(" select new " +
        "jpabook.jpashop_v2.dto.OrderSimpleQueryDto(o.id, m.name," +
        "o.orderDate, o.status, d.address) " +
        "  from Order o " +
        " join o.member m " +
        " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
  }
  /**
   *  select
   *         order0_.order_id as col_0_0_,
   *         member1_.name as col_1_0_,
   *         order0_.order_date as col_2_0_,
   *         order0_.status as col_3_0_,
   *         delivery2_.address as col_4_0_,
   *         delivery2_.street as col_4_1_,
   *         delivery2_.zipcode as col_4_2_
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

