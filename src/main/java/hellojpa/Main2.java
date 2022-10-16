package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberType;
import hellojpa.jpql.Team;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team1 = new Team();
            team1.setName("teamA");
            em.persist(team1);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setAge(10);
            member1.setTeam(team1);
            member1.setType(MemberType.ADMIN);

            em.persist(member1);

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);


            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setAge(20);
            member2.setTeam(team2);
            member2.setType(MemberType.USER);

            em.persist(member2);

            em.flush();
            em.clear();

            /**
             * 조건식(case)
             */

            /**
             * case
             *      when ...
             *      then ...
             *      else ...
             * end
             */
//            String query =
//                    "select " +
//                        "case when m.age <= 10 then '학생요금' " +
//                        "     when m.age >= 60 then '경로요금' " +
//                        "     else '일반요금' " +
//                        "end " +
//                    "from Member m";

            /**
             * coalesce : null 일 때 대체해서 나오는 어구
             */
//            String query = "select coalesce(m.username, '이름 없는 회원') as username from Member m";
//            List<String> result = em.createQuery(query, String.class)
//                    .getResultList();

            /**
             * nullif : 만약 해당 값이 있으면 null 반환 --> 값을 숨길 때 사용
             */
//            String query = "select nullif(m.username, '관리자') as username from Member m";
//            List<String> result = em.createQuery(query, String.class)
//                    .getResultList();

//            for (String s : result) {
//                System.out.println("s = " + s);
//            }

            /**
             * jpql 함수 (function)
             */

            /**
             * concat
             */

//            String query = "select concat('a', 'b') from Member m";
//            String query = "select 'a' || 'b' from Member m";

            /**
             * substring
             */
//            String query = "select substring(m.username, 2, 3) from Member m";

            /**
             * locate
             */

//            String query = "select locate('de', 'abcdefg') from Member m";

            /**
             * locate
             */

//            String query = "select size(t.members) from Team t";

            /**
             * index
             * @OrderColumn
             */

//            String query = "select size(t.members) from Team t";

            /**
             * 사용자 정의 함수 호출
             */
//            String query = "select function('group_concat', m.username) from Member m";
//            String query = "select group_concat(m.username) from Member m";

//            List<String> result = em.createQuery(query, String.class)
//            List<Integer> result = em.createQuery(query, Integer.class)
//                    .getResultList();

//            for (String s : result) {
//                System.out.println("s = " + s);
//            }

//            for (Integer integer : result) {
//                System.out.println("integer = " + integer);
//            }

            /**
             * 경로 표현식
             */

            /**
             * 상태 필드
             * - 경로 탐색의 끝, 탐색 X
             */
//            String query = "select m.username from Member m";
//
//            List<String> resultList = em.createQuery(query, String.class)
//                    .getResultList();

            /**
             * 단일 값 연관 경로
             * - 묵시적 내부 조인 (inner join) 발생, 탐색 O
             */

//            List<String> result = em.createQuery("select m.team.name from Member m", String.class)
//                    .getResultList();
//
//            for (String s : result) {
//                System.out.println("s = " + s);
//            }

//            List<Team> result = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();
//
//            for (Team team : result) {
//                System.out.println("team = " + team);
//            }

            /**
             * 컬렉션 값 연관 경로
             * - 묵시적 내부 조인 (inner join) 발생, 탐색 X
             */

//            String query = "select t.members from Team t";
//            List<Collection> resultList = em.createQuery(query, Collection.class)
//                    .getResultList();
//
//            System.out.println("resultList = " + resultList);

//            for (Object o : resultList) {
//                System.out.println("o = " + o);
//            }

//            String query = "select t.members.size from Team t";
//            List<Integer> resultList = em.createQuery(query, Integer.class)
////                    .getSingleResult();
//                    .getResultList();
////            System.out.println("resultList = " + resultList);
//            for (Integer integer : resultList) {
//                System.out.println("integer = " + integer);
//            }

            /**
             * 컬렉션 값 연관경로 --> 탐색 X --> from절 명시적 조인 --> 별칭 얻기  --> 탐색
             */

            String query = "select m from Team t join t.members m";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member.getUsername() = " + member.getUsername());
            }

            /**
             * 묵시적 조인 X, 명시적 조인 활용 권장!
             */


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
