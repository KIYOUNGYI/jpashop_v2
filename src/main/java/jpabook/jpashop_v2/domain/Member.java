package jpabook.jpashop_v2.domain;


import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// member <---> team n:1
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @JsonIgnore
  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();


  public Member(String name) {
    this(name, 0);
  }


  public Member(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  public Member(String name, int age) {
    this(name, age, null);
  }

  public Member(String name, int age, Team team) {
    this.name = name;
    this.age = age;
    if (team != null) {
      changeTeam(team);
    }
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

  public Member(String name, Address address, Team team) {
    this.name = name;
    this.address = address;
    this.team = team;
  }

  @Builder
  public Member(String name, Address address, Team team, Integer age) {
    this.name = name;
    this.address = address;
    this.team = team;
    this.age = age;
  }


}