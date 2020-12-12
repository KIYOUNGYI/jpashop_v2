package jpabook.jpashop_v2.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Parent {

  @Id
  @GeneratedValue
  @Column(name = "parent_id")
  private Long id;

  private String name;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Child> childList = new ArrayList<>();

  public void addChild(Child child) {
    childList.add(child);//원래는 이전에 데이터 있으면 빼고 그래야 하는데, 그것까진 구현 안함.
    child.setParent(this);
  }

}
