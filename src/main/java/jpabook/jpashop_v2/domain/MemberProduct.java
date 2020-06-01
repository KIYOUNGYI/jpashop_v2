package jpabook.jpashop_v2.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProduct
{
    @Id
    @GeneratedValue
    @Column(name="MEMBER_PRODUCT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name ="MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    private int orderAmount;

    private LocalDateTime orderDateTime;


}
