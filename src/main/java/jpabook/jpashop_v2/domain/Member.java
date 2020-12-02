package jpabook.jpashop_v2.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// member <---> team n:1
@Entity
@Getter
@Setter
@ToString(of = {"id", "name", "age"})
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  @NotEmpty
  private String name;

  private Integer age;

  @Embedded
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

  // 기본 생성자는 필수
  public Member() {
  }

  public Member(String name) {
    this.name = name;
  }

  public Member(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Member(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  public Member(String name, Address address, Team team) {
    this.name = name;
    this.address = address;
    this.team = team;
  }

  public Member(String name, Address address, Team team, Integer age) {
    this.name = name;
    this.address = address;
    this.team = team;
    this.age = age;
  }


}