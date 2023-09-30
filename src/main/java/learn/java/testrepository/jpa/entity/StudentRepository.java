package learn.java.testrepository.jpa.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s join fetch s.studentClass")
    List<Student> findAllWithStudentClass();

    @Query("select s from Student s where s.studentClass.id  in :classIds")
    List<Student> findAllStudentsInClasses(@Param("classIds") List<Long> classIds);
}
