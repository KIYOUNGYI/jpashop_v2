package jpabook.jpashop_v2.api;

import jpabook.jpashop_v2.domain.Address;
import jpabook.jpashop_v2.domain.Order;
import jpabook.jpashop_v2.domain.OrderItem;
import jpabook.jpashop_v2.domain.OrderStatus;
import jpabook.jpashop_v2.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
* V1. 엔티티 직접 노출
* - 엔티티가 변하면 API 스펙이 변한다.
* - 트랜잭션 안에서 지연 로딩 필요
* - 양방향 연관관계 문제 *
* V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
* - 트랜잭션 안에서 지연 로딩 필요
* V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
* - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경
가능) *
*V4.JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1+NQuery)
* - 페이징 가능
*V5.JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1+1Query)
* - 페이징 가능
* V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
* - 페이징 불가능... *
**/

@RestController
@RequiredArgsConstructor
public class OrderApiController
{
    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리 * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1()
    {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화 order.getDelivery().getAddress(); //Lazy 강제 초기환
            List<OrderItem> orderItems = order.getOrderItems(); orderItems.stream().forEach(o -> o.getItem().getName()); //Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2()
    {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }


    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3()
    {
        List<Order> orders = orderRepository.findAllWithItem();

        for(Order order: orders)
        {
            System.out.println("Order ref = "+order+" id="+order.getId());
        }

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * order 입장에서 toone 에 해당하는 건, member, delivery 이건 fetch join 걸어도 됨, 이건 data row 수가 늘어나는건 아니니.
     * @return
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_1_page()
    {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        //n+1 문제가 터지겠지

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }


    @Data
    static class OrderDto
    {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;//주문시간
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order)
        {
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
    static class OrderItemDto
    {
        private String itemName;//상품 명
        private int orderPrice;//주문 가격
        private int count;//주문 수량

        public OrderItemDto(OrderItem orderItem)
        {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
