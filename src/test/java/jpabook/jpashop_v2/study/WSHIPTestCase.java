package jpabook.jpashop_v2.study;

import jpabook.jpashop_v2.domain.Game;
import jpabook.jpashop_v2.domain.GameStore;
import jpabook.jpashop_v2.repository.wship.GameRepository;
import jpabook.jpashop_v2.repository.wship.GameStoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class WSHIPTestCase {

  @Autowired
  GameStoreRepository gameStoreRepository;

  @Autowired
  GameRepository gameRepository;


  @Test
  @Rollback(false)
  public void test() {
    GameStore gameStore = new GameStore();
    gameStore.setStoreName("게임1");
    gameStoreRepository.save(gameStore);

    Game game = new Game();
    game.setGameTitle("상점1");
    gameRepository.save(game);
    gameStore.add(game);
  }

  @Test
  @Rollback(false)
  public void test002() {

    Game game = new Game();
    game.setGameTitle("헤일로");
    gameRepository.save(game);

    GameStore gameStore = new GameStore();
    gameStore.setStoreName("시애틀");
    gameStore.add(game);
    gameStoreRepository.save(gameStore);

  }
}
