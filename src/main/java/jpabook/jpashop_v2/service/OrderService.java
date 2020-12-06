package jpabook.jpashop_v2.service;

import jpabook.jpashop_v2.domain.*;
import jpabook.jpashop_v2.repository.ItemRepository;
import jpabook.jpashop_v2.repository.MemberJpaRepository;
import jpabook.jpashop_v2.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  주문
 *  취소
 *  검색
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService
{
    private final OrderRepository orderRepository;
    private final MemberJpaRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count)
    {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송 정보
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        // 주문 상품 생성(예제 단순화시킴)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(),count);

//        OrderItem or = new OrderItem(); // 제약
        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId)
    {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
        //dirty-checking ~~ 변경 감지하면서 디비에 업데이트 쿼리가 촥촥 날라감
        //beauty of JPA.
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch)
    {
        return orderRepository.findAll(orderSearch);

    }

    //검색
    public List<Order> findAll()
    {
        return orderRepository.findAll();
    }
}
