package learn.java.testrepository.jpa.entity;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    @Query("select c from Class c join fetch c.students")
    Page<Class> findPagedClass(Pageable pageable);

    @Query("select c from Class c join fetch c.students")
    List<Class> findClassAll();
}
