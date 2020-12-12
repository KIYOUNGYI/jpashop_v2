package jpabook.jpashop_v2;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

  private final InitMemberService initMemberService;

  @PostConstruct
  public void init() {
//        initMemberService.init();
  }

  @Component
  static class InitMemberService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void init() {
      Team teamX = new Team("teamX");
      Team teamY = new Team("teamY");
      em.persist(teamX);
      em.persist(teamY);

      for (int i = 0; i < 200; i++) {
        Team selectedTeam = i % 2 == 0 ? teamX : teamY;
        em.persist(new Member("d_member_" + i, null, selectedTeam, i));
      }
    }
  }
}
