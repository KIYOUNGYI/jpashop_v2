package jpabook.jpashop_v2.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.Order;
import jpabook.jpashop_v2.domain.OrderItem;
import jpabook.jpashop_v2.domain.OrderSearch;
import jpabook.jpashop_v2.domain.OrderStatus;
import jpabook.jpashop_v2.repository.OrderRepository;
import jpabook.jpashop_v2.repository.query.OrderFlatDto;
import jpabook.jpashop_v2.repository.query.OrderItemQueryDto;
import jpabook.jpashop_v2.repository.query.OrderQueryDto;
import jpabook.jpashop_v2.repository.query.OrderQueryRepository;
import jpabook.jpashop_v2.service.query.OrderQueryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1. 엔티티 직접 노출 - 엔티티가 변하면 API 스펙이 변한다. - 트랜잭션 안에서 지연 로딩 필요 - 양방향 연관관계 문제 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X) - 트랜잭션 안에서 지연 로딩 필요 V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O) - 페이징 시에는 N 부분을 포기해야함(대신에
 * batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능) * V4.JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1+NQuery) - 페이징 가능 V5.JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1+1Query) - 페이징 가능 V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
 * - 페이징 불가능... *
 **/

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;
  private final OrderQueryService orderQueryService;

  @GetMapping("/api/v1/orders/querydsl")
  public List<Order> ordersV1QueryDsl() {
    OrderSearch orderSearch = new OrderSearch();
    List<Order> all = orderRepository.findAll(orderSearch);
    for (Order order : all) {
      order.getMember().getName(); //Lazy 강제 초기화
      order.getDelivery().getAddress(); //Lazy 강제 초기화
      List<OrderItem> orderItems = order.getOrderItems();// orderItems 이것도 초기화 하고,
      orderItems.stream().forEach(o -> o.getItem().getName()); // 그 안에 개개인 item 또한 이름을 가져오기 위해 초기화 한다.
    }
    return all;
  }

  /**
   * V1. 엔티티 직접 노출 - Hibernate5Module 모듈 등록, LAZY=null 처리 * - 양방향 관계 문제 발생 -> @JsonIgnore
   */
  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAll();
    for (Order order : all) {
      order.getMember().getName(); //Lazy 강제 초기화
      order.getDelivery().getAddress(); //Lazy 강제 초기화
      List<OrderItem> orderItems = order.getOrderItems();// orderItems 이것도 초기화 하고,
      orderItems.stream().forEach(o -> o.getItem().getName()); // 그 안에 개개인 item 또한 이름을 가져오기 위해 초기화 한다.
    }
    return all;
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAll();
    List<OrderDto> result = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());
    return result;
  }


  @GetMapping("/api/v3/orders-not-expected")
  public List<OrderDto> orderV3NotExpected() {
    List<Order> orders = orderRepository.findAllWithItemNoDistinct();

    for (Order order : orders) {
      System.out.println("Order ref = " + order + " id=" + order.getId());
    }

    List<OrderDto> result = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());
    return result;
  }

  /**
   * db의 distinct + 어플리케이션에서 가져와서 한 번 더 걸러줌.
   *
   * @return
   */
  @GetMapping("/api/v3/orders")
  public List<OrderDto> orderV3() {
    List<Order> orders = orderRepository.findAllWithItem();

    for (Order order : orders) {
      System.out.println("Order ref = " + order + " id=" + order.getId());
    }

    List<OrderDto> result = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());
    return result;
  }

  //osiv 껏을 때 대책
  @GetMapping("/api/v3/orders/osiv")
  public List<OrderQueryService.OrderDto> orderV3Osiv() {
    List<OrderQueryService.OrderDto> orders = orderQueryService.findAllWithItemOsivOff();
    return orders;
  }


  /**
   * order 입장에서 toone 에 해당하는 건, member, delivery 이건 fetch join 걸어도 됨, 이건 data row 수가 늘어나는건 아니니.
   *
   * @return
   */
  @GetMapping("/api/v3.1/orders/temp")
  public List<OrderDto> ordersV3_1() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();

    for (Order order : orders) {
      System.out.println("==> order:" + order.getId());
    }
    System.out.println("break");
    //n+1 문제가 터지겠지 (그런데 옵션에 배치 사이즈 넣어주면 해결됨)

    List<OrderDto> result = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());
    return result;
  }

  //갓영한님이 선호하는 페이징 방식
  //다대일,일대일 먼저 긁어오고
  //그다음 루프든,스트림이든 dto 채워넣는 것 (알아서 in 절 working~ (default_batch_size 설정해주면)
  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> ordersV3_1_copy(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "20") int limit
  ) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

//    for (Order order : orders) {
//      System.out.println("==> order:" + order.getId());
//    }

    List<OrderDto> result = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());
    return result;
  }

  //jpa에서 dto 직접 조회
  //아래 orderdto 쓰면 순환 참조 문제 생기기도 하고, (레포지토리가 컨트롤러를 참조하는 의존관계 순환)
  //1+n 어쩔 수 없음
  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDtos();
  }

  @GetMapping("/api/v4/orders/{id}")
  public List<OrderQueryDto> ordersV4one(@PathVariable Long id) {
    return orderQueryRepository.findOrderQueryDtos(id);
  }

  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> ordersV5() {
    return orderQueryRepository.findAllByDtoOptimiaztion();
  }

  @GetMapping("/api/v5.1/orders")
  public List<OrderQueryDto> ordersV5Paging(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "20") int limit
  ) {
    return orderQueryRepository.findAllByDtoOptimiaztion(offset, limit);
  }

  //order 를 기준으로 페이징이 불가능하다 (치명적 단점)
  @GetMapping("/api/v6/orders")
  public List<OrderQueryDto> ordersV6() {
    List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoFlat();
    //이제부터 중복을 내가 걸러내면 된다.
    //flatdto -> querydto 로 바꾸고, orderItemQuerydto 로 발라내는건데, 최종적으로 orderQueryDto 로 만드는데, e.getValue() 로 넘겨주고, 최종적으로 OrderQueryDto 로 반환, (이걸 메모리에서 해주는거죵)
    List<OrderQueryDto> collect = flats.stream().collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList()))).entrySet().stream()
        .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
        .collect(toList());

    return collect;
  }


  @Data
  static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;
    private int orderItemsSize;

    public OrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
      orderItemsSize = order.getOrderItems().size();
      orderItems = order.getOrderItems()
          .stream()
          .map(orderItem -> new OrderItemDto(orderItem))
          .collect(toList());
    }
  }

  @Data
  static class OrderItemDto {

    private String itemName;//상품 명
    private int orderPrice;//주문 가격
    private int count;//주문 수량

    public OrderItemDto(OrderItem orderItem) {
      itemName = orderItem.getItem().getName();
      orderPrice = orderItem.getOrderPrice();
      count = orderItem.getCount();
    }
  }

}
