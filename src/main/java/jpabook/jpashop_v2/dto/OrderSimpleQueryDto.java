package jpabook.jpashop_v2.dto;

import java.time.LocalDateTime;
import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderSimpleQueryDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;

  public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
  }

}
