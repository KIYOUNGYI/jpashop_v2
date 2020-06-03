package jpabook.jpashop_v2;

import jpabook.jpashop_v2.domain.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb
{
    private final InitService initService;


    @PostConstruct
    public void init() {
//        initService.dbInit1();
//        initService.dbInit2();
//        initService.dbInit3();
//        initService.dbInit4();
//        initService.dbInit5();
          initService.dbInit6();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService
    {
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);
            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 10000, 2);
            Order order = Order.createOrder(member, createDelivery(member),
                    orderItem1, orderItem2);
            em.persist(order);
        }
        public void dbInit2() {
            Member member = createMember("userB", "진주", "2", "2222");
            em.persist(member);
            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);
            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);
            Delivery delivery = createDelivery(member);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        public void dbInit3()
        {
            Member member = createMember("userC", "부산", "2", "2222");
            em.persist(member);
            Book book1 = createBook("Network 101", 20000, 200);
            em.persist(book1);
            Book book2 = createBook("Network 201", 40000, 300);
            em.persist(book2);
            Delivery delivery = createDelivery(member);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        public void dbInit4()
        {
            Member member = createMember("userD", "춘천", "2", "2222");
            em.persist(member);
            Book book1 = createBook("CLoud Architecture 101", 20000, 200);
            em.persist(book1);
            Book book2 = createBook("CLoud Architecture 201", 40000, 300);
            em.persist(book2);
            Delivery delivery = createDelivery(member);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        public void dbInit5()
        {
            Member member = createMember("userE", "파주", "2", "2222");
            em.persist(member);
            Book book1 = createBook("Python 101", 20000, 200);
            em.persist(book1);
            Book book2 = createBook("Python 201", 40000, 300);
            em.persist(book2);
            Delivery delivery = createDelivery(member);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        public void dbInit6()
        {
            Book book1 = createBook("Python 301", 20000, 600);
            em.persist(book1);
            for(int i=0;i<10;i++)
            {
                String x = "user_"+Integer.toString(i);
                Member member = createMember(x,"서울","1","dummyzip");
                em.persist(member);
                Delivery delivery = createDelivery(member);
                OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 1);
                Order order = Order.createOrder(member, delivery, orderItem1);
                em.persist(order);
            }

            Book book2 = createBook("Jpa 101", 10000, 10000000);
            Book book3 = createBook("Spring 101", 10000, 10000000);
            em.persist(book2);
            em.persist(book3);
            for(int i=0;i<2000;i++)
            {
                String x = "user__"+Integer.toString(i);
                Member member = createMember(x,"대전","1","dummyzip");
                em.persist(member);
                Delivery delivery = createDelivery(member);
                OrderItem orderItem1 = OrderItem.createOrderItem(book2, 20000, 2);
                OrderItem orderItem2 = OrderItem.createOrderItem(book3, 30000, 3);
                Order order = Order.createOrder(member, delivery, orderItem1,orderItem2);
                em.persist(order);
            }
        }

        private Member createMember(String name, String city, String street,
                                    String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }
        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }



}
