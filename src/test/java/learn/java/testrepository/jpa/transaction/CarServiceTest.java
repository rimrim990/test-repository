package learn.java.testrepository.jpa.transaction;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

@SpringBootTest
@DisplayName("JPA 트랜잭션 테스트")
class CarServiceTest {

    @Autowired
    private CarService carService;
    @PersistenceContext
    EntityManager em;

    @DisplayName("트랜잭션 전파 속성 REQUIRED - 이너 스코프 트랜잭션이 롤백되면 아우터 스코프가 커밋할 때 UnexpectedRollback 에외가 발생한다")
    @Test
    void propagationRequired_unexpectedRollback() {
        // when & then
        assertThatThrownBy(() -> carService.getCar(1L))
            .isInstanceOf(UnexpectedRollbackException.class);
    }

    @DisplayName("트랜잭션 전파 속성 REQUIRED - 트랜잭션 커넥션 테스트")
    @Test
    void propagationRequired_connection() {
        carService.getWithSameTransaction(1L);
    }

    @DisplayName("트랜잭션 전파 속성 REQUIRES_NEW - 트랜잭션 커넥션 테스트")
    @Test
    void propagationRequiresNew_connection() {
        carService.getWitNewTransaction(1L);
    }
}