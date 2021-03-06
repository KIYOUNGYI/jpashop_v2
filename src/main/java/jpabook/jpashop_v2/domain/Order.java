package jpabook.jpashop_v2.domain;

import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;


  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private Member member; //주문 회원

//  @BatchSize(size = 1000)//컬렉션인 경우엔 이리 하고
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();


  @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery; //배송정보
  //orderDate -> order_date 이렇게 바꾼다.(내부적으로)
  private LocalDateTime orderDate; //주문시간 (java 8 <- 하이버네이트 자동 지원?)

  @Enumerated(EnumType.STRING)
  private OrderStatus status; //주문상태 [ORDER, CANCEL]

  public Order(Member member, Delivery delivery) {
    this.member = member;
    this.delivery = delivery;
  }

  //==연관관계 메서드==//
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  // === 생성 메서드 ==//
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order(member, delivery);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }

  // 비즈니스 로직

  /**
   * 주문 취소
   */
  public void cancel() {
    if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 제품은 취소가 불가능합니다.");
    }
    this.setStatus(OrderStatus.CANCEL);

    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }

  // 조회 로직

  /**
   * 조회 로직 전체 주문 가격 조회 로직
   */
//    public int getTotalPrice()
//    {
//        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//        System.out.println("totalPrice:"+ totalPrice);
//        return totalPrice;
//    }
  public int getTotalPrice() {
    int totalPrice = 0;
    for (OrderItem orderItem : orderItems) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }

  //인텔리제이가 해줌
  public int getTotalPriceUsingMap() {
    return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
  }


}