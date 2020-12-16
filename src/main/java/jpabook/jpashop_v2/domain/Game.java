package jpabook.jpashop_v2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "game")
@Setter
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "game_id")
  private Long id;

  private String isbn;

  private String gameTitle;

  @ManyToOne
  @JoinColumn(name = "game_store_id")
  private GameStore gameStore;

  @Builder
  public Game(String isbn, String gameTitle, GameStore gameStore) {
    this.isbn = isbn;
    this.gameTitle = gameTitle;
    this.gameStore = gameStore;
  }


}
