package learn.java.testrepository.jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("스프링 JPA 영속성 컨텍스트 학습 테스트")
public class PersistentEntityTest {

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
    @Transactional
    @DisplayName("영속성 컨텍스트는 동일 트랜잭션 내에서는 읽기 동일성을 보장한다")
    void repeatableRead() {
        // give
        final Student student = studentRepository.findById(1L).get();

        // when
        final Student result = studentRepository.findById(student.getId()).get();

        // then
        assertThat(result).isEqualTo(student);
    }

    @Test
    @Transactional
    @DisplayName("영속성 컨텍스트는 프록시 객체도 읽기 동일성을 보장한다")
    void proxy_repeatableRead() {
        // give
        final Student student = studentRepository.findById(1L).get();
        final Class classProxy = student.getStudentClass();

        // when
        final Class result = em.getReference(Class.class, classProxy.getId());

        // then
        // 프록시 객체도 영속성 컨텍스트에 저장된다
        assertThat(result == classProxy).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("프록시 객체를 초기화하면 영속성 컨텍스트에 실제 객체가 올라간다")
    void proxy_initialize() {
        // give
        final Student student = studentRepository.findById(1L).get();
        final Class classProxy = student.getStudentClass();

        // when
        classProxy.getName();

        // then
        assertThat(classRepository.findById(classProxy.getId())).hasValue(classProxy);
    }
}
