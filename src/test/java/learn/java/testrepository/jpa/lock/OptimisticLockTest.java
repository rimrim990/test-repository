package learn.java.testrepository.jpa.lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("JPA 낙관적 락 테스트")
class OptimisticLockTest {

    @Autowired
    private VersionEntityRepository versionRepository;

    @Autowired
    private VersionEntityService versionService;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("낙관적 락은 트랜잭션 커밋 시점에 버전 정보를 증가시킨다")
    void version_update() {
        // given
        final VersionEntity entity = new VersionEntity("test");
        versionRepository.save(entity);

        // when
        entity.updateTitle("hi");
        final VersionEntity result = versionRepository.save(entity);

        // then
        assertThat(result.getVersion()).isEqualTo(entity.getVersion()+1);
    }

    @Test
    @DisplayName("엔티티를 수정하지 않으면 버전 정보는 변하지 않는다")
    void version_notUpdate() {
        // given
        final VersionEntity entity = new VersionEntity("test");
        versionRepository.save(entity);

        // when
        final VersionEntity result = versionRepository.save(entity);

        // then
        assertThat(result.getVersion()).isEqualTo(entity.getVersion());
    }

    @Test
    @DisplayName("버전 정보가 일치하지 않으면 충돌이 발생했다고 판단하여 예외를 던진다")
    @Transactional
    void version_conflict() {
        // given
        final Long id = versionService.save(new VersionEntity("test"));
        final VersionEntity entity = versionRepository.findById(id).get();

        // when - 다른 트랜잭션에서 엔티티 업데이트
        versionService.updateTitle(id);

        entity.updateTitle("bye");
        versionRepository.save(entity);

        // then - 영속성 컨텍스트가 flush 할 때 버전 정보가 달라 예외 발생
        assertThatThrownBy(() -> em.flush())
            .isInstanceOf(OptimisticLockException.class);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("OPTIMISTIC 락 모드는 데이터를 조회할 때 버전을 체크하고 커밋하기 전에 한 번 더 체크한다")
    void version_optimistic() {
        // given
        final Long id = versionService.save(new VersionEntity("test"));

        // when
        versionRepository.findByIdWithOptimisticLock(id).get();
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("OPTIMISTIC 락 모드는 트랜잭션 안에서 일관적 읽기를 보장한다")
    void version_optimistic_conflict() {
        // given
        final Long id = versionService.save(new VersionEntity("test"));

        // when
        versionRepository.findByIdWithOptimisticLock(id).get();

        // 다른 트랜잭션에서 엔티티 수정
        versionService.updateTitle(id);
    }

    @Transactional
    @Test
    @Rollback(value = false)
    @DisplayName("낙관적 락은 스칼라 타입으로 조회할 때는 사용할 수 없다")
    void version_optimistic_scalar() {
        // given
        final Long id = versionService.save(new VersionEntity("test"));

        // when
        versionRepository.findTitleWithOptimisticLock(id).get();

        // 다른 트랜잭션에서 엔티티 수정
        versionService.updateTitle(id);
    }
}