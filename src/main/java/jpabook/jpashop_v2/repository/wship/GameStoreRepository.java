package jpabook.jpashop_v2.repository.wship;

import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.GameStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameStoreRepository {

  private final EntityManager em;

  public void save(GameStore gameStore) {
    em.persist(gameStore);
  }
}
