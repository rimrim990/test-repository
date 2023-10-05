package learn.java.testrepository.jpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("엔티티 그래프 학습 테스트")
public class EntityGraphTest {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void setUp() {
        List<Student> hiStudents = List.of(
            new Student("a", "1"), new Student("b", "2"), new Student("c", "3")
        );
        List<Student> helloStudents = List.of(
            new Student("d", "4"), new Student("e", "5"), new Student("f", "6")
        );

        Class hi = new Class("hi");
        hi.addStudent(hiStudents);
        classRepository.save(hi);

        Class hello = new Class("hello");
        hello.addStudent(helloStudents);
        classRepository.save(hello);

        em.clear();
    }

    @Test
    @DisplayName("엔티티 그래프를 LOAD 로 설정하면 그래프에 표기한 속성들을 FetchType.EAGER 로 가져온다")
    void entityGraph() {
        // when
        List<Student> students = studentRepository.findAll();

        // then
        System.out.println("=================");
        for (Student student : students) {
            student.getStudentClass().getName();
        }
    }

    @Test
    @DisplayName("엔티티 그래프는 left 조인만 지원한다")
    void entityGraph_onlyLeftJoin() {
        classRepository.findAll();
    }

}
