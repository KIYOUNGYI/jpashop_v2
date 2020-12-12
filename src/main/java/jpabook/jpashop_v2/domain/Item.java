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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * JOINED 전략을 쓰게 되면 id 는 pk 이면서 동시에 fk
 * dtype은 왠만하면 넣어두는게 좋음. (관례 DTYPE 따르는게 편리할듯?)
 * dtype은 single table 에는 들어가야 함.
 *
 * 싱글테이블은 성능이 잘 나오는 편.
 *
 * (table_per_class) 구현 클래스마다 테이블로 만드는 전략은 아이템 테이블의 속성을 자식들로 다 끌어내리는것 -> 비추
 * discriminator가 의미 없음. 이 전략에서는
 * 데이터를 넣을 때는 문제가 없는데, 조회할 때가 문제가됨. 테이블 다 찾아봐야함. (union)
 * Item item = em.find(Item.class, movie.getId()); //부모로 조회하면 자식들 다 찾는다 정도
 *
 * 그리고 부모 녀석은 abstract 으로 구현해주는게 맞음 -> 그냥 class Item 을 쓴다는건, 상속과 상관없이 Item 독단적으로 쓰일 수 있다는 것.
 * 제약을 걸어두는게 맞음.
 *
 * 조인전략 장점
 * -> 외래키 참조 무결성 제약조건 활용가능 (자식 테이블도 pk,fk 가 걸려있는것이니 굳)
 * -> 저장공간 개이득
 * 조인저략 단점
 * -> 싱글테이블 대비 조인 많이 사용, 쿼리 복잡, 데이터 저장시 insert 2번 호출(어드민이면 괜찮을거 같은데,클라쪽은 그럴 수 있겠다)
 *
 * 단일테이블 전략
 * [장점]
 * 조회성능 좋아~
 * 조회쿼리 단순~
 * [단점]
 * name, price 빼고 다 null 허용해줘야함.
 * 테이블 커지면 오히려 성능이 느려질 수 있 (얼마나??)
 *
 * 구현클래스 전략 -> 쓰지마~
 * 장점
 * not null 제약 조건 사용 가능
 * 서브 타입 명확하게 구분해서 처리할 때 효과적
 * 단점
 * union sql 써야함.(통합해서 쿼리하기 어렵)
 *
 * 영한님은 조인전략을 보통 기본으로 하는데,
 * 단순할 땐, 확장가능할 일도 없을 땐, 단일테이블 전략을 많이 선택함.
 * 비즈니스적으로 뭔가 중요할 땐 조인 전략
 *
 * 그런데 이건 어째든 트레이드 오프가 있는거고 회사 룰 따르는게 맞음
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
@NoArgsConstructor
public abstract class Item {

  @Id @GeneratedValue
  @Column(name = "item_id")
  private Long id;
  private String name;

  private int price;
  private int stockQuantity;

  public Item(String name, int price, int stockQuantity) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }

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
