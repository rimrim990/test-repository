package learn.java.testrepository.jpa.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.TransactionRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@DisplayName("트랜잭션 전파 속성 학습 테스트")
public class PropagationTest {

    @Autowired
    PropagationService propagationService;

    /**
     * 기존 트랜잭션에 참여한다 - REQUIRED, SUPPORTS, MANDATORY
     */
    @Test
    @DisplayName("REQUIRED - 트랜잭션이 존재하지 않으면 생성한다")
    void required_create() {

        // when & then
        assertThat(propagationService.required()).isNotBlank();
    }

    @Test
    @Transactional
    @DisplayName("REQUIRED - 트랜잭션이 존재하면 기존 트랜잭션에 참여한다")
    void required_participate() {
        // given
        final String curTx = TransactionSynchronizationManager.getCurrentTransactionName();

        // when & then
        assertThat(propagationService.required()).isEqualTo(curTx);
    }

    @Test
    @DisplayName("SUPPORTS - 트랜잭션이 존재하지 않으면 트랜잭션을 사용하지 않는다")
    void supports_notCreate() {

        // when & then
        assertThatThrownBy(() -> propagationService.supports())
            .isInstanceOf(TransactionRequiredException.class)
            .hasMessage("No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call");
    }

    @Test
    @Transactional
    @DisplayName("SUPPORTS - 트랜잭션이 존재하면 기존 트랜잭션에 참여한다")
    void supports_participate() {
        // given
        final String curTx = TransactionSynchronizationManager.getCurrentTransactionName();

        // when & then
        assertThat(propagationService.supports()).isEqualTo(curTx);
    }

    @Test
    @DisplayName("MANDATORY - 트랜잭션이 존재하지 않으면 예외를 던진다")
    void mandatory_throwException() {

        // when & then
        assertThatThrownBy(() -> propagationService.mandatory())
            .isInstanceOf(IllegalTransactionStateException.class)
            .hasMessage("No existing transaction found for transaction marked with propagation 'mandatory'");
    }

    @Test
    @Transactional
    @DisplayName("MANDATORY - 트랜잭션이 존재하면 기존 트랜잭션에 참여한다")
    void mandatory_participate() {
        // given
        final String curTx = TransactionSynchronizationManager.getCurrentTransactionName();

        // when & then
        assertThat(propagationService.mandatory()).isEqualTo(curTx);
    }

    /**
     * 트랜잭션을 사용하지 않는다 - NOT_SUPPORTED, NEVER
     */
    @Test
    @Transactional
    @DisplayName("NOT_SUPPORTS - 기존 트랜잭션을 중단시키고 트랜잭션을 사용하지 않는다")
    void notSupported_suspend() {

        // when & then
        assertThatThrownBy(() -> propagationService.notSupported())
            .isInstanceOf(TransactionRequiredException.class)
            .hasMessage("No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call");
    }

    @Test
    @Transactional
    @DisplayName("NEVER - 트랜잭션이 존재하면 예외를 던진다")
    void never_throwException() {

        // when & then
        assertThatThrownBy(() -> propagationService.never())
            .isInstanceOf(IllegalTransactionStateException.class)
            .hasMessage("Existing transaction found for transaction marked with propagation 'never'");
    }

    /**
     * 항상 새로운 트랜잭션을 생성한다 - REQUIRES_NEW, NESTED
     */
    @Test
    @Transactional
    @DisplayName("REQUIRES_NEW - 항상 새로운 트랜잭션을 생성한다")
    void requiresNew_create() {
        final String curTx = TransactionSynchronizationManager.getCurrentTransactionName();

        // when & then
        assertThat(propagationService.requiresNew()).isNotEqualTo(curTx);
    }

    @Test
    @Transactional
    @DisplayName("NESTED - 항상 새로운 트랜잭션을 생성한다")
    // 체크포인트를 지원하지 않아 사용 불가능하다
    void nested() {
        final String curTx = TransactionSynchronizationManager.getCurrentTransactionName();

        // when & then
        assertThat(propagationService.nested()).isNotEqualTo(curTx);
    }
}
