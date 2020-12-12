package jpabook.jpashop_v2.inheritence;

import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest @Transactional
@Rollback(false)
public class InheritenceTest {

  @Autowired EntityManager entityManager;

  @Test
  public void di(){
    Movie movie = new Movie("Back to the future 4",2500,10,"xxx","zzz");
    System.out.println("movie:"+movie.toString());
    entityManager.persist(movie);
  }
}
