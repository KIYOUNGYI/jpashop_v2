package jpabook.jpashop_v2.api;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.Order;
import jpabook.jpashop_v2.domain.OrderStatus;
import jpabook.jpashop_v2.repository.OrderRepository;
import jpabook.jpashop_v2.repository.OrderSimpleQueryDto;
import jpabook.jpashop_v2.repository.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (ManyToOne, OneToOne) -> 어떻게 성능최적화를 할 까? Order -> Member (사용자는 여러개 주문할 수 있고) Order -> Delivery (한 주문은 한 딜리버리를 뜻하는 것이고) 해결방법 -> 양방향 걸리는거 JsonIgnore 걸어줘야함.
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  private final OrderSimpleQueryRepository orderSimpleQueryRepository;

  /**
   * 이렇게 하면 무슨 문제가 있을까??? 무한루프 (ㅋㅋㅋㅋ) 혼난다. 양방향 문제 발생, 해결하려면 jsonignore -> 그래도 proxy 문제 발생 hibernate5Module 를 빈으로 등록해서 사용
   *
   * @return
   */
  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1() {

    //order -> member -> order -> member -> order .... 무한 루프
    List<Order> all = orderRepository.findAll();
    for (Order order : all) {
      order.getMember().getName();//LAZY 강제 초기화 (order.getMember()<-프록시 객체임) 그런데 getName() 까지하면 강제 초기화 된다.
      order.getDelivery().getAddress();//LAZY 강제 초기화
    }
    return all;
  }

  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAll();
    //sql 1번 -> 결과 주문수 2개

    // 2개
    // 루프가 돌 때 , member,delivery 조회,
    // 2ㅜ번째 돌 때 name,address 2번 또 가져온거임
    // 총 5번
    // 소위 1+n 문제 발생
    // N + 1 -> 1 + 회원 N + 배송 N (같은 회원이면 1번만 찌르긴 한데)
    List<SimpleOrderDto> result = orders.stream()
        .map(o -> new SimpleOrderDto(o))
        .collect(toList());
    return result;
  }

  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> orderV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    List<SimpleOrderDto> result = orders.stream()
        .map(o -> new SimpleOrderDto(o))
        .collect(toList());
    return result;
  }

  //v3는 재사용성이 좋은 반면, v4는 재사용성이 별로임. 그리고 우열을 가리기 힘듬
  //성능면에서는 v4가 좀 더 좋다. v4 코드 지저분 // 요즘 네트워크 차이가 별로 안나서 크게 성능 차이 안남.
  //api 스펙에 맞춘 코드가 곧 단점임
  @GetMapping("/api/v4/simple-orders")
  public List<OrderSimpleQueryDto> ordersV4() {
    return orderSimpleQueryRepository.findOrderDtos();
  }


  @Data
  static class SimpleOrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();// LAZY 초기화 , 영속성 컨텍스트 찾아가서 없으면 db에 질의
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();// LAZY 초기화
    }
  }


}
