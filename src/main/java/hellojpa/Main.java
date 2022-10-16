package hellojpa;

import hellojpa.jpql.*;

import javax.persistence.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            for (int i = 0; i < 100; i++) {

                Team team = new Team();
                team.setName("teamA");
                em.persist(team);

                Member member = new Member();
                member.setUsername("teamA");
                member.setAge(10);
                member.setType(MemberType.ADMIN);

                member.setTeam(team);

                em.persist(member);
//            }
            em.flush();
            em.clear();

            /**
             * TypeQuery, Query
             */
//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//            Query query3 = em.createQuery("select m.username, m.age from Member m");

            /**
             * make an inquiry - single or list
             */
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            //collection result
//            List<Member> resultList = query1.getResultList();
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//            }
//            System.out.println("======================================");
//
//            //single result
//            Member singleResult = query1.getSingleResult();
//            //Spring Data JPA -> return not noResultException but null
//            System.out.println("singleResult = " + singleResult);

            /**
             * parameter binding
             */
//            Member result = em.createQuery("select m from Member m where m.username=:username", Member.class)
//                    .setParameter("username", "member1")
//                    .getSingleResult();
//            System.out.println("result = " + result.getUsername());

            /**
             * projection
             */

//            List<Member> result = em.createQuery("select m.team from Member m", Member.class)
//                    .getResultList();
//
//            Member findMember = result.get(0);
//            findMember.setAge(20);

//            List<Team> singleResult1 = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();
//            List<Team> singleResult1 = em.createQuery("select t from Member m join m.team t", Team.class)
//                    .getResultList();

            /**
             * 임베디드 프로젝션
             */
//            List<Address> resultList = em.createQuery("select o.address from Order o", Address.class)
//                    .getResultList();

            // 스칼라 타입 프로젝션, 여러 값 조회

            //1
//            List resultList = em.createQuery("select distinct m.username, m.age from Member m")
//                    .getResultList();
//            Object o = resultList.get(0);
//            Object[] result = (Object[]) o;
//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);

            //2
//            List<Object[]> resultList2 = em.createQuery("select m.username, m.age from Member m", Object[].class)
//                    .getResultList();
//            //or
//            List<Object[]> resultList3 = em.createQuery("select m.username, m.age from Member m")
//                    .getResultList();
//            Object[] result1 = resultList2.get(0);
//            System.out.println("result1.username = " + result1[0]);
//            System.out.println("result1.age = " + result1[1]);
//            Object[] result2 = resultList3.get(0);
//
//            System.out.println("result2.username = " + result2[0]);
//            System.out.println("result2.age = " + result2[1]);

            /**
             * DTO를 이용한 여러 값 조회
             */
//            List<MemberDTO> result4 = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age)  from Member m", MemberDTO.class)
//                    .getResultList();
//
//            MemberDTO memberDTO = result4.get(0);
//            System.out.println("memberDTO.username = " + memberDTO.getUsername());
//            System.out.println("memberDTO.age = " + memberDTO.getAge());

            /**
             * 페이징
             */
//            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(2)
//                    .setMaxResults(10)
//                    .getResultList();
//            System.out.println("result.size() = " + result.size());
//            for (Member member1 : result) {
//                System.out.println("member1 = " + member1);
//            }

            /**
             * 조인
             */
////            String query = "select m from Member m inner join m.team t where t.name = :teamName"; //inner join
////            String query = "select m from Member m join m.team t where t.name = :teamName"; //(inner) join & param
////            String query = "select m from Member m left outer join m.team t";//left outer join
////            String query = "select m from Member m left join m.team t"; //left (outer) join
////            String query = "select m from Member m, Team t where m.username = t.name"; //세타조인
////            String query = "select m from Member m left join m.team t on t.name = 'teamA'"; //조인 대상 필터링
////            String query = "select m from Member m left join Team t on m.username = t.name"; //연관관계 없는 엔티티 사이 외부조인
//            String query = "select m from Member m join Team t on m.username = t.name"; //연관관계 없는 엔티티 사이 내부조인
//
////            List<Member> result = em.createQuery(query, Member.class)
////                    .setParameter("teamName", "teamA")
////                    .getResultList();
//
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();
//
////            for (Member member1 : result) {
////                System.out.println("member1.getUsername() = " + member1.getUsername());
////                System.out.println("member1.team.getName() = " + member1.getTeam().getName());
////            }
//            System.out.println("result.size() = " + result.size());

            /**
             * 서브쿼리
             */
//            String query = "select (select avg(m1.age) from Member m1) as avgAge from Member m join Team t on m.username=t.name";
////            String query = "select mm.age, mm.username from " +
////                    "(select m.age, m.username from Member m) as mm"; // from 서브쿼리 -> JPA 사용 불가
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();
//            System.out.println("result = " + result);

            /**
             * JPQL 타입 표현
             */

//            String query = "select m.username, 'Hello', true, m.type from Member m " +
//                    "where m.type = :userType";
//
//            query = "select m.username, 'Hello', true, m.type from Member m " +
//                    "where m.type is not null";
//
//            query = "select m.username, 'Hello', true, m.type from Member m " +
//                    "where m.age between 0 and 10";
//
//
////            List<Object[]> resultList = em.createQuery(query)
////                    .setParameter("userType", MemberType.ADMIN)
////                    .getResultList();
//
//            List<Object[]> resultList = em.createQuery(query)
//                    .getResultList();
//            for (Object[] objects : resultList) {
//                System.out.println("objects = " + objects[0]);
//                System.out.println("objects = " + objects[1]);
//                System.out.println("objects = " + objects[2]);
//                System.out.println("objects = " + objects[3]);
//            }

            /**
             * 조건식(case)
             */









            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        System.out.println("Main.main----------------finished");
        emf.close();
    }
}