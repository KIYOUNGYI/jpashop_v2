package jpabook.jpashop_v2.domain;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("M")
@Getter
//@Setter
@NoArgsConstructor(access = PROTECTED)
public class Movie extends Item {

  private String director;
  private String actor;

  public Movie(String name, int price, int stockQuantity, String director, String actor) {
    super(name, price, stockQuantity);

    this.director = director;
    this.actor = actor;

  }

}
