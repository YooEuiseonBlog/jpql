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
             * fetch join (페치 조인) ***
             * - 다대일 연관관계
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

            /**
             * 페치 조인의 특징과 한계
             * - 페치 조인 대상에는 별칭을 줄 수 없다.
             *  ㄴ 하이버네이트는 가능, 가급적 사용 X
             * - 둘 이상의 컬렉션은 페치 조인 할 수 없다. ---> 페치 조인 컬렉션은 딱 하나만! 조인할 수 있다.
             * - 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다. ---> 컬렉션 페치 조인은 데이터 뻥튀기가 가능하기 때문이다.
             *  ㄴ 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
             *  ㄴ 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
             *  --------------------------------------------------------------------------------------------------------------------------------
             * - 연관된 엔티티들을 SQL 한 번으로 조회 -성능 최적화
             * - 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
             *  ㄴ @OneToMany(fetch=FetchType.LAZY) //글로벌 로딩 전략
             * - 실무에서 글로벌 전략은 모두 지연 로딩
             * - 최적화가 필요한 곳은 페치 조인 적용
             */

            /**
             * 페치 조인 - 정리 ***
             * - 모든 것을 페치 조인으로 해결할 수는 없음
             * - 페치 조인은 객체 그래프(경로 표현식처럼 XXX.xxx 같이 탐색할 때)를 유지할 때 사용하면 효과적
             * - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
             *   페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
             *      ㄴ   1. fetch join 사용해서 Entity를 조회한다.
             *      ㄴ   2. fetch join -> application에서 dto로 바꿔서 화면에 반환한다.
             *      ㄴ   3. jpql new operation를 활용해서 dto로 스위칭(switching)해서 가지고 온다.
             */

//            String qlString = "select t from Team t join t.members m"; // join은 하나 필드를 해당 엔티티 필드(속성)값만 가져옴
//            String qlString = "select t from Team t join fetch t.members m"; // paging api를 사용할 수 없다. 따라서 일대다가 아닌 다대일 연관관계에서 접근한다.
//            String qlString = "select m from Member m join fetch m.team t"; // paging api를 사용할 수 없다.  해결책1 : 일대다 --> 다대일
            String qlString = "select t from Team t";
//            String qlString = "select t from Team t join fetch t.members m join fetch m.team"; // 다중 조인 페치할 때만 별칭(alias) 사용하자!
            List<Team> result = em.createQuery(qlString, Team.class)
//            List<Member> result = em.createQuery(qlString, Member.class)
                    .setFirstResult(0)  //  일대다(컬렉션 페치 조인) : WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result = " + result.size());

            System.out.println("============= loop ============");

            for (Team team : result) {
                System.out.println("team = " + team.getName() +"|member = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-->     member = " + member);
                }
            }
//            for (Member member : result) {
//                System.out.println("member.team.name = " +member.getTeam().getName());
//            }

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
