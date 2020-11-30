package jpabook.jpashop_v2.repository;

import static jpabook.jpashop_v2.domain.QMember.member;
import static jpabook.jpashop_v2.domain.QTeam.team;
import static org.springframework.util.StringUtils.hasText;
import static org.thymeleaf.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import jpabook.jpashop_v2.domain.Member;
import jpabook.jpashop_v2.dto.MemberSearchCondition;
import jpabook.jpashop_v2.dto.MemberTeamDto;
import jpabook.jpashop_v2.dto.QMemberTeamDto;
import org.springframework.stereotype.Repository;

//import static org.springframework.util.StringUtils.isEmpty;

@Repository
//@RequiredArgsConstructor
public class MemberJpaRepository {

  //    [방법1]
//    @PersistenceContext @Autowired
//    private EntityManager em;
//    //스프링이 em을 만들어서 주입해줌
//
//    [방법2]
  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public MemberJpaRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  // 원래는 persistencecontext 가 어노테이션 표준인데, 스프링부트(datajpa)가 autowired 도 지원해줌

  public Long save(Member m) {
    em.persist(m);
    return m.getId();
  }

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  public List<Member> findAll() {
    // JPQL
    // sql은 테이블을 대상으로 퀘리를 날리는데, JPQL은 객체를 대상으로 쿼리를 날림
    return em.createQuery("select m from Member m", Member.class)
        .getResultList();
  }

  public List<Member> findByName(String name) {
    return em.createQuery("select m " +
        "from Member m " +
        "where m.name = :name", Member.class)
        .setParameter("name", name)//parameter binding
        .getResultList();
  }

  public Optional<Member> findById(Long id) {
    Member findMember = em.find(Member.class, id);
    return Optional.ofNullable(findMember);
  }

  /**
   * dsl
   **/
  public List<Member> findAll_Querydsl() {
    return queryFactory
        .selectFrom(member).fetch();
  }

  public List<Member> findByUsername_Querydsl(String name) {

    System.out.println("=======start=======");
    List<Member> result = queryFactory
        .selectFrom(member)
        .where(member.name.eq(name))
        .fetch();
    System.out.println("======= end =======");

    return result;
  }


  /**
   * Builder 사용 회원명, 팀명, 나이(ageGoe, ageLoe)
   *
   * @param condition
   * @return
   */
  public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {

    BooleanBuilder builder = new BooleanBuilder();
    if (hasText(condition.getUsername())) {
      builder.and(member.name.eq(condition.getUsername()));
    }
    if (hasText(condition.getTeamName())) {
      builder.and(team.name.eq(condition.getTeamName()));
    }
    if (condition.getAgeGoe() != null) {
      builder.and(member.age.goe(condition.getAgeGoe()));
    }
    if (condition.getAgeLoe() != null) {
      builder.and(member.age.loe(condition.getAgeLoe()));
    }
    return queryFactory
        .select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.name,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName")))
        .from(member)
        .leftJoin(member.team, team)
        .where(builder)
        .fetch();
  }

  /**
   * 회원명, 팀명, 나이(ageGoe, ageLoe)
   *
   * @param condition
   * @return
   */
  public List<MemberTeamDto> search(MemberSearchCondition condition) {
    return queryFactory
        .select(new QMemberTeamDto(
            member.id.as("memberId"),
            member.name,
            member.age,
            team.id.as("teamId"),
            team.name.as("teamName")))
        .from(member)
        .leftJoin(member.team, team)
        .where(usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageGoe(condition.getAgeGoe()),
            ageLoe(condition.getAgeLoe()))
        .fetch();
  }


  /**
   * //where 파라미터 방식은 이런식으로 재사용이 가능하다.
   *
   * @param condition
   * @return
   */
  public List<Member> findMember(MemberSearchCondition condition) {
    return queryFactory
        .selectFrom(member)
        .leftJoin(member.team, team)
        .where(usernameEq(condition.getUsername()),
            teamNameEq(condition.getTeamName()),
            ageGoe(condition.getAgeGoe()),
            ageLoe(condition.getAgeLoe()))
        .fetch();
  }

  private BooleanExpression usernameEq(String username) {
    return isEmpty(username) ? null : member.name.eq(username);
  }

  private BooleanExpression teamNameEq(String teamName) {
    return isEmpty(teamName) ? null : team.name.eq(teamName);
  }

  private BooleanExpression ageGoe(Integer ageGoe) {
    return ageGoe == null ? null : member.age.goe(ageGoe);
  }

  private BooleanExpression ageLoe(Integer ageLoe) {
    return ageLoe == null ? null : member.age.loe(ageLoe);
  }
}
