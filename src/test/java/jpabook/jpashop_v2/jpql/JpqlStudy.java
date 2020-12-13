package jpabook.jpashop_v2.jpql;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JpqlStudy {

  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("상태필드 가지고 검색")
  public void di() {
    String query = "select m.name From Member m";

    List<String> result = em.createQuery(query, String.class).getResultList();
    for (String s : result) {
      System.out.println("s=" + s.toString());
    }
  }

  @Test
  @DisplayName("단일 값 연관경로 가지고 검색, 멤버,팀,(manyToOne)관계 ")
  public void di002() {
    String query = "select m.team From Member m";
    //select m.team From Member m */

    // select team1_.team_id as team_id1_11_, team1_.name as name2_11_
    // from member member0_
    // inner join team team1_ on member0_.team_id=team1_.team_id;

    List<Team> result = em.createQuery(query, Team.class).getResultList();
    for (Team s : result) {
      System.out.println("s=" + s.toString());
    }
  }

  @Test
  @DisplayName("컬렉션 연관관 ")
  public void di003() {
    String query = "select t.members From Team t";
    //select m.team From Member m */

    // select team1_.team_id as team_id1_11_, team1_.name as name2_11_
    // from member member0_
    // inner join team team1_ on member0_.team_id=team1_.team_id;

    Collection result = em.createQuery(query, Collection.class).getResultList();
    for (Object o : result) {
      System.out.println("s=" + o.toString());
    }
  }

  public void di004() {
    String query = "select m From Team t join t.members m";
  }

  @Test
  public void di005() {
    String query = "select m From Member m join fetch m.team";
    List<Member> resultList = em.createQuery(query, Member.class).getResultList();
    System.out.println(resultList.toString());
  }

  @Test
  public void di006(){
    Team teamX = new Team("teamX");
    Team teamY = new Team("teamY");

  }
}
