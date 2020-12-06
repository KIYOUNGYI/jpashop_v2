package jpabook.jpashop_v2.service.query;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.Order;
import jpabook.jpashop_v2.domain.OrderItem;
import jpabook.jpashop_v2.domain.OrderStatus;
import jpabook.jpashop_v2.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

  private final OrderRepository orderRepository;

  public List<OrderDto> findAllWithItemOsivOff(){
    List<Order> orders = orderRepository.findAllWithItem();
    for (Order order : orders) {
      System.out.println("Order ref = " + order + " id=" + order.getId());
    }

    List<OrderDto> result = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());
    return result;
  }

  @Data
  public static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
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
