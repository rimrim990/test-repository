package learn.java.testrepository.jpa.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("스프링 N+1 문제 학습 테스트")
public class LazyFetchTest {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void setUp() {
        List<Student> hiStudents = List.of(
            new Student("a"), new Student("b"), new Student("c")
        );
        List<Student> helloStudents = List.of(
            new Student("d"), new Student("e"), new Student("f")
        );

        Class hi = new Class("hi");
        hi.addStudent(hiStudents);
        classRepository.save(hi);

        Class hello = new Class("hello");
        hello.addStudent(helloStudents);
        classRepository.save(hello);
    }


    @Test
    @DisplayName("지연 로딩으로 조회하면 연관관계에 프록시 객체가 할당된다")
    void lazy_proxy() {
        // given
        Student student = studentRepository.findById(1L).get();

        // when
        Class studentClass = student.getStudentClass();

        // then
        assertThat(studentClass.getId()).isNotNull();
        assertThatThrownBy(() -> studentClass.getName())
            .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @Transactional // 지연 로딩으로 프록시를 초기화하려면 영속성 컨텍스트가 있어야 함
    @DisplayName("지연 로딩 후 프록시 객체를 초기화하면 N+1 문제가 발생한다")
    void lazy_nPlusOne() {
        // given
        em.clear();

        List<Student> students = studentRepository.findAll();

        // when
        for (Student student : students) {
            student.getStudentClass().getName();
        }
    }

    @Test
    @DisplayName("패치 조인을 사용하면 N+1 문제 없이 연관관계를 함께 가져올 수 있다")
    void lazy_fetchJoin() {
        // given
        List<Student> students = studentRepository.findAllWithStudentClass();

        // when
        for (Student student : students) {
            student.getStudentClass().getName();
        }
    }

    @Test
    @Transactional
    @DisplayName("@OneToMany 연관관계에서는 연관관계 리스트의 원소에 접근하는 즉시 프록시가 초기화된다")
    void lazy_OneToMany() {
        // given
        em.clear();

        Class studentClass = classRepository.findById(1L).get();

        // when
        studentClass.getStudents().get(0);
    }

    @Test
    @DisplayName("@OneToMany 연관관계에서는 데이터가 불어나지만 하이버네이트에서 자동으로 처리해주기 때문에 distinct 더이상 가 필요 없다")
    void lazy_OneToMany_distinct() {
        // given
        List<Class> studentClasses = classRepository.findClassAll();

        // when
        assertThat(studentClasses).hasSize(2);
    }

    @Test
    @DisplayName("@OneToMany 연관관계에서 패치 조인과 페이징을 함께 사용하면 메모리에서 페이징이 일어난다")
    void oneToMany_memoryPaging() {
        // when
        classRepository.findAll(PageRequest.ofSize(1));
    }

    @Test
    @Transactional
    @DisplayName("@BatchSize 를 사용하면 size 크기 만큼의 엔티티와 연관된 데이터 조회를 하나의 쿼리로 수행한다")
    void batchSize() {
        // given
        em.clear();

        Page<Class> studentClasses = classRepository.findAll(PageRequest.ofSize(2));

        //when
        for (Class studentClass : studentClasses.getContent()) {
            studentClass.getStudents().get(0);
        }
    }

    @Test
    @Transactional
    @DisplayName("@BatchSize 를 사용하면 단 건 쿼리를 조회하더라도 항상 size 만큼 IN 쿼리를 생성한다")
    void batchSize_alwaysIn() {
        // given
        em.clear();

        Class studentClass = classRepository.findById(1L).get();

        //when
        studentClass.getStudents().get(0);
    }

    @Test
    @DisplayName("@BatchSize 를 사용하기 보다는 두 개의 쿼리를 별도로 실행하여 조합하는 것이 좋을 것 같다")
    void splitQueries() {
        // given
        em.clear();

        // when
        Page<Class> studentClasses = classRepository.findAll(PageRequest.ofSize(10));
        List<Long> classIds = studentClasses.getContent()
            .stream()
            .map(Class::getId)
            .toList();

        Map<Long, List<Student>> allStudentsInClasses = studentRepository.findAllStudentsInClasses(classIds)
            .stream()
            .collect(Collectors.groupingBy(student -> student.getStudentClass().getId()));

        for (Class studentClass : studentClasses.getContent()) {
            studentClass.setStudents(allStudentsInClasses.get(studentClass.getId()));
        }

    }
}
