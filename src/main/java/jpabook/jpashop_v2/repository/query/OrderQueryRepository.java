package jpabook.jpashop_v2.repository.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

  private final EntityManager em;

  //쿼리dsl 을 사용해야 하는 이유... n+1 문제가 있음
  public List<OrderQueryDto> findOrderQueryDtos() {
    List<OrderQueryDto> result = findOrders();// query 1번 -> N 개
    result.forEach(o -> {
      List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); //Query N번
      o.setOrderItems(orderItems);
    });
    return result;
  }

  //단건인 경우에는 v4 방식이 더 이득일 수 있음 (직관적이니까) 쿼리도 2개밖에 안 나가는데 어차피
  public List<OrderQueryDto> findOrderQueryDtos(Long id) {
    List<OrderQueryDto> result = findOrder(id);// query 1번 -> N 개
    result.forEach(o -> {
      List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); //Query N번
      o.setOrderItems(orderItems);
    });
    return result;
  }

  //1대 다 해결하려면 이렇게 따로 별도로 빼서 해야함. 어쩔 수 없음 (v4 관련)
  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery("select new jpabook.jpashop_v2.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
        " from OrderItem oi" +
        " join oi.item i" +
        " where oi.order.id = :orderId", OrderItemQueryDto.class).setParameter("orderId", orderId).getResultList();
  }

  //orderItems는 제외시킴 어쩔 수 없음
  private List<OrderQueryDto> findOrders() {
    return em.createQuery("select new jpabook.jpashop_v2.repository.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
        + " from Order o" +
        " join o.member m" +
        " join o.delivery d", OrderQueryDto.class).getResultList();
  }

  private List<OrderQueryDto> findOrder(Long orderId) {
    return em.createQuery("select new jpabook.jpashop_v2.repository.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
        + " from Order o" +
        " join o.member m" +
        " join o.delivery d where o.id = :orderId", OrderQueryDto.class).setParameter("orderId", orderId).getResultList();
  }

  private List<OrderQueryDto> findOrders(int offset, int limit) {
    return em.createQuery("select new jpabook.jpashop_v2.repository.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
        + " from Order o" +
        " join o.member m" +
        " join o.delivery d", OrderQueryDto.class).setFirstResult(offset).setMaxResults(limit).getResultList();
  }

  //v5  (v4 에 비해 성능이 엄청 최적화 됨, 1+n 에서 1+1 이 되었다.) 나머지는 메모리에서 제조립 (리팩토링 해서 그렇지 여러 단계를 거침)
  public List<OrderQueryDto> findAllByDtoOptimiaztion() {
    //루트 조회(toOne 코드를 모두 한번에 조회)
    List<OrderQueryDto> result = findOrders();// query 1번 -> N 개

    List<Long> orderIds = toOrderIds(result);

    //orderItem 컬렉션을 MAP 한방에 조회(주문 데이터만큼, 메모리에 올리는 것)
    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

    //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X) (모잘랐던 데이터 보완해주는 것)
    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

    return result;
  }

  //v5 paging
  public List<OrderQueryDto> findAllByDtoOptimiaztion(int offset, int limit) {
    //루트 조회(toOne 코드를 모두 한번에 조회)
    List<OrderQueryDto> result = findOrders(offset, limit);// query 1번 (n개 return)

    List<Long> orderIds = toOrderIds(result);

    //orderItem 컬렉션을 MAP 한방에 조회(주문 데이터만큼, 메모리에 올리는 것)
    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

    //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X) (모잘랐던 데이터 보완해주는 것)
    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

    return result;
  }

  private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
    List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook.jpashop_v2.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
        " from OrderItem oi" +
        " join oi.item i" +
        " where oi.order.id in :orderId", OrderItemQueryDto.class)
        .setParameter("orderId", orderIds)
        .getResultList();

    //앞의 것과의 차이는, 메모리에 맵으로 이녀석을 다 가져온 다음, 메모리에서 매칭을 해서 값을 세팅해 주는 것이다. 이렇게 하면 쿼리가 총 2번 나가게 됩니다.
    //key -> orderItemQueryDto.getOrderId(), value -> List<OrderItemQueryDto>
    return orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
  }

  //todo  Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) <- querydsl 버전으로 만들기!


  private List<Long> toOrderIds(List<OrderQueryDto> result) {
    return result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
  }


  public List<OrderFlatDto> findAllByDtoFlat() {
    return em.createQuery(
        "select new jpabook.jpashop_v2.repository.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
            " from Order o" +
            " join o.member m" +
            " join o.delivery d" +
            " join o.orderItems oi" +
            " join oi.item i", OrderFlatDto.class)
        .getResultList();

  }


}
