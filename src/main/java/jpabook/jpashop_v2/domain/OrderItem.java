package jpabook.jpashop_v2.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@Table(name="order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  OrderItem
{
    @GeneratedValue
    @Id @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;//주문

    @Column(name = "order_price")
    private int orderPrice;// 주문 가격

    private int count;// 주문 수량

    // 생성자 쓰지마라는 경고 NoArgsConstructor 와 동일한 효과
//    protected OrderItem(){}

    // 생성 메서도
    public static OrderItem createOrderItem(Item item,int orderPrice,int count)
    {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        System.out.println("count:"+count+" and remove stock");
        item.removeStock(count);
        return orderItem;
    }


    // 비즈니스 로직
    public void cancel()
    {
        getItem().addStock(count);
    }

    /**
     * 주문상품 전체 가격 조회
     * @return
     */
    public int getTotalPrice()
    {
        System.out.println("getOrderPrice():"+getOrderPrice());
        System.out.println("getCount():"+getCount());
        return getOrderPrice()*getCount();
    }


}
