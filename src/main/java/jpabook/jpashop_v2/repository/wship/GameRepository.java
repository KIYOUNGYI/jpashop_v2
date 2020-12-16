package jpabook.jpashop_v2.repository.wship;

import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameRepository {

  private final EntityManager em;

  public void save(Game game) {
    em.persist(game);
  }
}
