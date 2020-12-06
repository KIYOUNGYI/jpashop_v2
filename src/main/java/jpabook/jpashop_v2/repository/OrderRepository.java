package jpabook.jpashop_v2.repository;

import static jpabook.jpashop_v2.domain.QMember.member;
import static jpabook.jpashop_v2.domain.QOrder.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Order;
import jpabook.jpashop_v2.domain.OrderSearch;
import jpabook.jpashop_v2.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;
  private final JPAQueryFactory query;

  @Autowired
  public OrderRepository(EntityManager em) {
    this.em = em;
    this.query = new JPAQueryFactory(em);
  }

  public void save(Order order) {
    try {
      em.persist(order);
    } catch (Exception e) {
      System.out.println("error ? " + e.toString());
    }

  }

  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }


  public List<Order> findAll() {
    List<Order> temp = em.createQuery("select o from Order o", Order.class).getResultList();
    return temp;
  }

  public List<Order> findAll(OrderSearch orderSearch) {
    //new operation 엄청 지저분했던 것, querydsl 썻던 것 훨씬 개선됨.
    return query.select(order).from(order).join(order.member, member)
        .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName())).limit(1000).fetch();

  }

  private BooleanExpression statusEq(OrderStatus statusCond) {
    if (statusCond == null) {
      return null;
    }
    return order.status.eq(statusCond);
  }

  private BooleanExpression nameLike(String nameCond) {
    if (!StringUtils.hasText(nameCond)) {
      return null;
    }
    return member.name.like(nameCond);
  }


  /**
   * fetch 는 jpa 에만 있는 문법. sql에는 없지... 실무에서 자주사용하는건데, 깊이가 생각이 깊음. 이거 100% 이해를 해야함.
   * <p>
   * 쿼리 1번에 다 가져오는 장점이 있다만, 단점은 굳이 뽑자면, 필요없는 필드도 많이 뽑아낸다 정도?
   *
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

  /**
   * distinct 키워드는 2가지 역할을 한다. 1] db 에 distinct 를 날린다. 2] root Entity 가 중복인 경우, 걸러서 콜렉션에 담아준다. 단점 >>> 페이징을 못한다. 페이징 필요한 api 는 이거 못쓴다. 글로우픽으로 치면, 어워드, 제품상세만 가능하고, cursor 포함된 api는 못쓰는것. 2020-06-02
   * 22:12:33.283  WARN 4210 --- [nio-8080-exec-1] o.h.h.internal.ast.QueryTranslatorImpl   : HHH000104: firstResult/maxResults specified with collection fetch; applying in memory! 페치 조인을 썻는데 페이징 쿼리가
   * 들어갔네? 메모리에서 (소팅)페이징 처리를 할거얌! 데이터가 100000건이었으면, 10000개를 앱으로 끌어올린다음에 out of memory 뜨는 극단적인 선택을 하겠죵.
   *
   * @return
   */
  public List<Order> findAllWithItem() {
    return em.createQuery(
        "select distinct o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch oi.item i", Order.class)
//                .setFirstResult(1)
//                .setMaxResults(100)
        .getResultList();
  }

  public List<Order> findAllWithItemNoDistinct() {
    return em.createQuery(
        "select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch oi.item i", Order.class).getResultList();
  }

  public List<Order> findAllWithMemberDelivery(int offset, int limit) {
    return em.createQuery(
        "select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d", Order.class)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }
}
