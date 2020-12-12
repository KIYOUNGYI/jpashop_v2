package jpabook.jpashop_v2.cascade;

import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Child;
import jpabook.jpashop_v2.domain.Parent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional

public class CascadeTest {

  @Autowired
  EntityManager entityManager;

  @Test
  @Rollback(false)
  public void di001() {
    Child child1 = new Child();
    Child child2 = new Child();
    child1.setName("c1");
    child2.setName("c2");


    Parent parent = new Parent();
    parent.setName("p1");
    parent.addChild(child1);
    parent.addChild(child2);

    entityManager.persist(parent);
    entityManager.persist(child1);
    entityManager.persist(child2);
    entityManager.flush();
  }
  //parent 가 child 를 자동으로 관리해주고 싶을 때. 이 때 쓰는게 cascade
  @Test
  @Rollback(false)
  public void di002() {
    Child child1 = new Child();
    Child child2 = new Child();
    Child child3 = new Child();
    child1.setName("c1");
    child2.setName("c2");
    child3.setName("c3");


    Parent parent = new Parent();
    parent.setName("p1");
    parent.addChild(child1);
    parent.addChild(child2);
    parent.addChild(child3);
    entityManager.persist(parent);//parent 만 persist 했다! 이게 캐스케이드임, 다른게 아니라. (연쇄)
    entityManager.flush();
  }

  @Test
  @Rollback(false)
  public void di003() {

    Child child1 = new Child();
    Child child2 = new Child();
    Child child3 = new Child();
    child1.setName("c1");
    child2.setName("c2");
    child3.setName("c3");


    Parent parent = new Parent();
    parent.setName("p2");
    parent.addChild(child1);
    parent.addChild(child2);
    parent.addChild(child3);
    entityManager.persist(parent);//parent 만 persist 했다! 이게 캐스케이드임, 다른게 아니라. (연쇄)
    entityManager.flush();
    entityManager.clear();

    Parent p1 = entityManager.find(Parent.class,parent.getId());
    entityManager.remove(p1);
    entityManager.flush();
    entityManager.clear();

  }

}
