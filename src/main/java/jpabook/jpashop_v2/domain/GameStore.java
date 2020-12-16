package jpabook.jpashop_v2.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_store")
@Getter
@Setter
@NoArgsConstructor
public class GameStore {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "game_store_id")
  private Long id;

  private String storeName;

  @OneToMany(mappedBy = "gameStore")
  private Set<Game> games = new HashSet<>();

  public void add(Game game) {
    game.setGameStore(this);//핵심 => 주인녀석에게 이 객체를 연결해줘야 한다.
    getGames().add(game);
  }

  @Builder
  public GameStore(String storeName, Set<Game> games) {
    this.storeName = storeName;
    this.games = games;
  }
}
