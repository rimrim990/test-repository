package learn.java.testrepository.jpa.entity;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where s.id = :id")
    Student findByIdQuery(@Param("id") Long id);

    @Query("select s from Student s join fetch s.studentClass")
    List<Student> findAllWithStudentClass();

    @Query("select s from Student s where s.studentClass.id  in :classIds")
    List<Student> findAllStudentsInClasses(@Param("classIds") List<Long> classIds);

    @EntityGraph(attributePaths = "studentClass", type = EntityGraphType.LOAD)
    List<Student> findAll();

    @Modifying
    @Query("update Student s set s.name = :name where s.id = :id")
    void updateStudentName(@Param("id") Long id, @Param("name") String name);

    @Modifying(clearAutomatically = true)
    @Query("update Student s set s.name = :name where s.id = :id")
    void updateStudentNameWithClear(@Param("id") Long id, @Param("name") String name);
}


