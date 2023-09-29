package learn.java.testrepository.jpa.lock;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VersionEntityRepository extends JpaRepository<VersionEntity, Long> {

    Optional<VersionEntity> findById(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select v from VersionEntity v where v.id = :id")
    Optional<VersionEntity> findByIdWithOptimisticLock(@Param("id") Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select v.title from VersionEntity v where v.id = :id")
    Optional<String> findTitleWithOptimisticLock(@Param("id") Long id);

}
