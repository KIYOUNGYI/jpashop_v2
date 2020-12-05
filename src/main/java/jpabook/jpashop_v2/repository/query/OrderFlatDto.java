package jpabook.jpashop_v2.repository.query;

import java.time.LocalDateTime;
import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderFlatDto {

  //order <-> orderitem join , orderitem <-> item join 해서 한번에 똭 가져오는 방법
  private Long orderId;
  private String name;
  private LocalDateTime orderDate; //주문시간 private Address address;
  private OrderStatus orderStatus;
  private Address address;
  private String itemName;//상품 명 private int orderPrice; //주문 가격 private int count; //주문 수량
  private int orderPrice;
  private int count;

  public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate,
      OrderStatus orderStatus, Address address, String itemName, int orderPrice, int
      count) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
    this.itemName = itemName;
    this.orderPrice = orderPrice;
    this.count = count;
  }
}
