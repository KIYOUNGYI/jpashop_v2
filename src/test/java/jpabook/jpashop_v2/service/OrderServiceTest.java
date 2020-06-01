package jpabook.jpashop_v2.service;

import jpabook.jpashop_v2.domain.*;
import jpabook.jpashop_v2.exception.NotEnoughStockException;
import jpabook.jpashop_v2.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 상품 주문이 성공해야한다.
 * 상품을 주문할 때 재고 수량을 초과하면 안된다.
 * 주문 취소가 성공해야 한다.
 */
@SpringBootTest
@Transactional
class OrderServiceTest
{
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception
    {
        //given
        Member member = createMember();

        Item book = createBook(10000, "JPA", 10);
        //when
        int orderCnt = 2;
        Long orderId = orderService.order(member.getId(),book.getId(),orderCnt);
        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER,getOrder.getStatus(),"상품 주문시 상태는 ORDER");
        assertEquals(1,getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 한다");
        assertEquals(10000*2, getOrder.getTotalPrice(),"주문 가격은 가격 * 수량이다");
        assertEquals(8, book.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다");
    }

    @Test
    public void 주문취소() throws Exception
    {
        //given
        Member member = createMember();
        Item book = createBook(10000,"JPA",10);

        //when
        Long orderId = orderService.order(member.getId(),book.getId(),3);
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL,getOrder.getStatus(),"상품 주문 취소시 상태는 CANCEL");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception
    {
        Member member = createMember();
        Item book = createBook(10000, "JPA", 10);
        int orderCnt = 12;
        try
        {
            orderService.order(member.getId(), book.getId(), orderCnt);
        }
        catch (NotEnoughStockException e)
        {
            System.out.println("error intended : " + e.toString());
            return;
        }
        fail("여기까지 도달하면 안되는건데...");
    }


    private Item createBook(int price, String name, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("aaa");
        member.setAddress(new Address("Seoul","booyoung","123123"));
        em.persist(member);
        return member;
    }
}