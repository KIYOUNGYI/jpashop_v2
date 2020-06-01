package jpabook.jpashop_v2.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product
{
    @Id
    @GeneratedValue
    @Column(name="PRODUCT_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy="product")//MemberProduct 에서 product 변수 //read
    private List<MemberProduct> members = new ArrayList<>();


}
