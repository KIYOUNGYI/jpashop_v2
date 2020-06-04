package jpabook.jpashop_v2.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

// member <---> team n:1
@Entity
@Getter @Setter
@ToString(of ={"id","name","age"})
public class Member
{
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @NotEmpty
    private String name;

    private Integer age;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member() {}
    public Member(String name)
    {
        this.name = name;
    }

    public Member(String name,Address address)
    {
        this.name = name;
        this.address = address;
    }

    public Member(String name,Address address,Team team)
    {
        this.name = name;
        this.address = address;
        this.team = team;
    }

    public Member(String name,Address address,Team team,Integer age)
    {
        this.name = name;
        this.address = address;
        this.team = team;
        this.age = age;
    }


}