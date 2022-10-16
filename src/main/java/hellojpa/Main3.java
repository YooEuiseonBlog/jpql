package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberType;
import hellojpa.jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main3 {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

//            Team teamC = new Team();
//            teamC.setName("teamC");
//            em.persist(teamC);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(20);
            member1.setType(MemberType.USER);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(15);
            member2.setType(MemberType.USER);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(20);
            member3.setType(MemberType.USER);
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            /**
             * fetch join (페치 조인)
             */

//            String qlString = "select m from Member m join fetch  m.team";
////            String qlString = "select m from Member m";
//            List<Member> result = em.createQuery(qlString, Member.class)
//                    .getResultList();
//            System.out.println("==================================================");
//            int i = 0;
//            for (Member member : result) {
//                i++;
//                System.out.println("member" + i + " = " + member.getUsername() + ", " + member.getTeam().getName());
//                //회원1, 팀A(SQL)
//                //회원2, 팀A(1차캐시-영속성 컨텍스트)
//                //회원3, 팀B(SQL)
//
//                //회원 100명 --> N(결과) + 1(첫번째 날린 쿼리)
//            }

            /**
             * 컬렉션 페치 조인 (Collection fetch join)
             * - 일대다 관계, 컬렉션 페치 조인 --> 데이터 뻥튀기 가능하다.
             */

////            String qlString = "select t from Team t join fetch t.members"; // team을 가진 멤버수를 조회 혹은 멤버를 가지고 있는 각 team 조회 (멤버 기준으로)
//            String qlString = "select distinct t from Team t join fetch t.members"; // 중복 제거
////            String qlString = "select t from Team t"; // team 개수를 조회
//            List<Team> result = em.createQuery(qlString, Team.class)
//                    .getResultList();
//
//            System.out.println("result = " + result.size());
//
//            for (Team team : result) {
//                System.out.println("team = " + team.getName() + "| members = "  + team.getMembers().size());
//                for (Member member : team.getMembers()) {
//                    System.out.println("-->  member = " + member);
//                }
//            }

            /**
             * 페치 조인와 일반 조인의 차이
             * 일반 조인
             * - 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음.
             * - JPQL은 결과를 반환할 때 연관관계 고려 X
             * - 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
             * - 여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회 X
             *
             * 페치 조인 --> N + 1은 대부분 해결
             * - 페치 조인은 연관된 엔티티를  함께 조회함
             * - 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시 로딩)
             * - 페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념
             */

            String qlString = "select t from Team t join t.members m"; // join은 하나 필드를 해당 엔티티 필드(속성)값만 가져옴
//            String qlString = "select t from Team t join fetch t.members m";
            List<Team> result = em.createQuery(qlString, Team.class)
                    .getResultList();

            System.out.println("result = " + result.size());

            System.out.println("====================================");

            for (Team team : result) {
                System.out.println("team = " + team.getName() +"|member = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-->     member = " + member);
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
