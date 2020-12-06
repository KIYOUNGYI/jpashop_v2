package jpabook.jpashop_v2.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrderSearch
{
    private String memberName; //회원 이름
    private OrderStatus orderStatus;//주문 상태[ORDER, CANCEL]
}
