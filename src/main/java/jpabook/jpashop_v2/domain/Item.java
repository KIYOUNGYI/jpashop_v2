package jpabook.jpashop_v2.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import jpabook.jpashop_v2.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

  @Id
  @GeneratedValue
  @Column(name = "item_id")
  private Long id;
  private String name;
  private int price;
  private int stockQuantity;

  //    @OneToMany(mappedBy = "item")
  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<Category>();

  /**
   * 재고 증가
   *
   * @param quantity
   */
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }

  public void removeStock(int quantity) {
    int resultStock = this.stockQuantity - quantity;
    System.out.println("stockQuantity:" + this.stockQuantity);
    System.out.println("input quantity:" + quantity);
    System.out.println(">>>> resultStock : " + resultStock);
    if (resultStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }
    this.stockQuantity = resultStock;
  }

}
