package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main4 {
    public static void main(String[] args) {

        /**
         * 다형성 쿼리
         */

        /**
         * TYPE
         * - 조회 대상을 특정 자식으로 한정
         * 예) Item 중에 Book, Movie를 조회해라.
         *
         * [JPQL]
         *  select i from Item i
         *  where  type(i) IN (Book, Movie)
         * [SQL]
         *  select i from i
         *  where i.DTYPE in ('B', 'M')
         */

        /**
         * TREAT(JPA 2.1)
         * - 자바의 타입 캐스팅과 유사
         * - 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용
         * - FROM, WHERE, SELECT(하이버네이트 지원) 사용
         *
         * 예 0_0) 부모인 Item과 자식 Book이 있다.
         *
         * [JPQL]
         *  select i from Item i
         *  where treat(i as Book).author = 'kim'
         * [SQL]
         *  select i.* from Item i
         *  where i.DTYPE = 'B' and i.author = 'kim'
         */

        /**
         * 엔티티 직접 사용 - 기본 키 값
         * - JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용
         *
         * [JPQL]
         *  select count(m.id) from Member m //엔티티의 아이디를 사용
         *  select count(m) from Member m //엔티티를 직접 사용
         *
         * [SQL](JPQL 둘다 같은 다음 SQL 실행)
         *  select count(m.id) as cnt from Member m
         *
         * 엔티티를 파라미터로 전달
         *  String jpql = "select m from Member m where m = :member";
         *  List resultList = em.createQuery(jpql)
         *                          .setParameter("member", member)
         *                          .getResultList();
         *
         *  식별자를 직접 전달
         *  String jpql = "select m from Member m where m.id = :memberId";
         *  List resultList = em.createQuery(jpql)
         *                      .setParameter("memberId", memberId)
         *                      .getResultList();
         *
         *  실행된 SQL
         *  select m.* from Member m where m.id=?
         */

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);

//            em.flush();
//            em.clear();

////            String query = "select m from Member m where m = :member";
//            String query = "select m from Member m where m.id = :memberId";
//            Member findMember = em.createQuery(query, Member.class)
////                    .setParameter("member", member1)
//                    .setParameter("memberId", member1.getId())
//                    .getSingleResult();
//
//            System.out.println("findMember = " + findMember);

            /**
             * 엔티티 직접 사용 - 외래 키 값
             */

////            String query = "select m from Member m where m.team = :team";
//            String query = "select m from Member m where m.team.id = :teamId";
//            List<Member> members = em.createQuery(query, Member.class)
////                    .setParameter("team", teamA)
//                    .setParameter("teamId", teamA.getId())
//                    .getResultList();
//
//            for (Member member : members) {
//                System.out.println("member = " + member);
//            }

            /**
             * Named 쿼리 -어노테이션
             * Named 쿼리 - 정적 쿼리
             * - 미리 정의해서 이름을 부여해두고 사용하는 JPQL
             * - 정적 쿼리
             * - 어노테이션, XML에 정의
             * - 애플리케이션 로딩 시점에 초기화 후 재사용
             * - *애플리케이션 로딩 시점에 쿼리를 검증
             *
             *  Named 쿼리 환경에 따른 설정
             * - xml이 항상 우선권을 가진다.
             * - 애플리케이션 운영 환경에 따라 다른 XML을 배포할 수 있다.
             */

//            List<Member> result = em.createNamedQuery("Member.findByUsername", Member.class)
//                    .setParameter("username", "회원1")
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member = " + member);
//            }

            /**
             * 벌크 연산
             * - 쿼리 한 번으로 여러 테이블 로우 변경(엔티티)
             * - executeUpdate()의 결과는 영향받은 엔티티 수 반환
             * - UPDATE, DELETE 지원
             * - INSERT(insert into .. select, 하이버네이트 지원)
             */

            /**
             * 벌크 연산 주의점
             * - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
             *  ㄴ 벌크 연산을 먼저 실행
             *  ㄴ 벌크 연산 수행 후 영속성 컨텍스트 초기화
             *  (createQuery이기 때문에 직전 flush됨.
             *  그러나 그 후에 변경된 내용을 영속성 컨텍스트에 저장하기 위해서는 clear()를 통해 한 번 초기화한 후 SQL 조회 권장!)
             */

            //FLUSH 자동 호출 commit, query, flush
            //모든 회원의 나이를 20살로 변경하기
            String query = "update Member m set m.age = 20";
            int resultCount = em.createQuery(query)
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);

            // 특정회원(회원1)만 나이를 30으로 변경하기
//            query = "update Member m set m.age = :age where m.username = :username";
//            resultCount = em.createQuery(query)
//                    .setParameter("age", 35)
//                    .setParameter("username", member1.getUsername())
//                    .executeUpdate();
//
//            System.out.println("EditCount = " + resultCount);
//            em.clear();

            // 멤버 리스트 조회
            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            for (Member member : members) {
                System.out.println("member = " + member);
            }

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember);
//
//            System.out.println("member1.getAge() = " + member1.getAge());
//            System.out.println("member2.getAge() = " + member2.getAge());
//            System.out.println("member3.getAge() = " + member3.getAge());



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
