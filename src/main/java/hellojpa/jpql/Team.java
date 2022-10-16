package hellojpa.jpql;

import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
//    @BatchSize(size = 100) // 요소로 배치사이즈를 설정할 수 있다. 그러나 글로벌 셋팅하는 것이 편하다.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
