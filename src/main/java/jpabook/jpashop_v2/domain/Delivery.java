package jpabook.jpashop_v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery
{
    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; //ENUM [READY(준비), COMP(배송)]



}
