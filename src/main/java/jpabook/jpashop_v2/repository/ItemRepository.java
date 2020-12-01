package jpabook.jpashop_v2.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final EntityManager em;

  public void save(Item item) {
    if (item.getId() == null) {
      em.persist(item);// 저장하기 전까진 id가 없음
    } else {
      em.merge(item);// update 비슷한 개념임(진짜 업뎃은 아님)
    }
  }

  public Item findOne(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class).getResultList();
  }
}
