package learn.java.testrepository.spring.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("스프링 이벤트 학습 테스트")
public class SpringEventTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Test
    @DisplayName("스프링 이벤트가 발행되면 이벤트가 핸들러가 동기적으로 실행된다")
    void event() {
        // given
        final PlainEvent event = new PlainEvent();

        // when
        System.out.println("before event publish");
        publisher.publishEvent(event);
        System.out.println("after event publish");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("beforeCommit 은 트랜잭션이 커밋하기 전에 실행된다")
    void transactionalEvent_beforeCommit() {
        // given
        final TransactionEvent event = new TransactionEvent();

        // when
        System.out.println("before event publish");
        // 이벤트 발행
        publisher.publishEvent(event);
        System.out.println("after event publish");

        // 실제 이벤트 핸들러가 수행되는 것은 트랜잭션이 끝난 후다 - triggerBeforeCompletion
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("afterCommit 은 트랜잭션 커밋이 끝난 후 실행된다")
    void transactionalEvent_afterCommit() {
        // given
        final TransactionEvent event = new TransactionEvent();

        // when
        System.out.println("before event publish");
        // 이벤트 발행
        publisher.publishEvent(event);
        System.out.println("after event publish");

        // 실제 이벤트 핸들러가 수행되는 것은 트랜잭션이 끝난 후다 - triggerAfterCompletion(STATUS_COMMIT)
    }

    @Test
    @Transactional
    @DisplayName("afterRollback 은 트랜잭션 롤백이 끝난 후 실행된다")
    void transactionalEvent_afterRollback() {
        // given
        final TransactionEvent event = new TransactionEvent();

        // when
        System.out.println("before event publish");
        // 이벤트 발행
        publisher.publishEvent(event);
        System.out.println("after event publish");

        // 실제 이벤트 핸들러가 수행되는 것은 트랜잭션이 끝난 후다 - triggerAfterCompletion(STATUS_ROLLBACK)
    }

    @Test
    @Transactional
    @DisplayName("afterCompletion 은 트랜잭션 커밋과 롤백 여부와 상관 없이 항상 실행된다")
    void transactionalEvent_afterCompletion() {
        // given
        final TransactionEvent event = new TransactionEvent();

        // when
        System.out.println("before event publish");
        // 이벤트 발행
        publisher.publishEvent(event);
        System.out.println("after event publish");

        // 실제 이벤트 핸들러가 수행되는 것은 트랜잭션이 끝난 후다
        // triggerAfterCompletion(STATUS_COMMITTED);
        // triggerAfterCompletion(STATUS_ROLLBACK)
    }
}
