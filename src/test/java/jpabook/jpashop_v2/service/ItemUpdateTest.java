package jpabook.jpashop_v2.service;

import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ItemUpdateTest {

  @Autowired
  EntityManager em;


  @Test
  @DisplayName("dirty checking")
//  @Rollback(false)
  public void dirty_checking_test() {
    //given
    Book book = em.find(Book.class, 9l);
    //when,then

    //tx
    book.setName("blahblah");
    //변경감지 == dirty checking
    //tx commit
  }

  @Test
  @DisplayName("merge test <-- isbn 빵꾸난거 봤지? 조심해야됨윰 ")
//  @Rollback(false)
  public void merge_test() {

    //given
    Book book = new Book();
    book.setId(68l);
    book.setName("blahblah");
    book.setPrice(10000);
    book.setStockQuantity(100);
    //when
    em.merge(book);
    //then
  }


  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    book.setIsbn("asdf");
    return book;
  }

  @Test
//  @Rollback(false)
  public void x() {
    Book book1 = createBook("JPA1 BOOK", 10000, 100);
    em.persist(book1);
  }
}
