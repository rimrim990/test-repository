package learn.java.testrepository.spring.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventHandlers {

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(value = PlainEvent.class)
    public void eventListener(final PlainEvent event) {
        System.out.println("EventHandlers.eventListener");
    }

    //@Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK, value = TransactionEvent.class)
    public void afterRollback(final TransactionEvent event) {
        // 트랜잭션 활성화 상태가 아니다 ACTIVE -> STATUS_ROLLBACK
        // 그러나 트랜잭션 자체는 존재하기 때문에 @Transaction 을 사용하면 기존 트랜잭션에 합류 가능
        // 그러나 트랜잭션이 ACTIVE 가 아니라 STATUS_ROLLBACK 이라서 `no transaction is in progress` 예외 발생
        entityManager.flush();
        System.out.println("EventHandlers.afterRollback");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION, value = TransactionEvent.class)
    public void afterCompletion(final TransactionEvent event) {
        // 트랜잭션 끝남
        System.out.println("EventHandlers.afterCompletion");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, value = TransactionEvent.class)
    public void afterCommit(final TransactionEvent event) {
        // 트랜잭션 끝남
        System.out.println("EventHandlers.afterCommit");
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, value = TransactionEvent.class)
    public void beforeCommit(final TransactionEvent event) {
        // 트랜잭션 아직 살아있음
        System.out.println("EventHandlers.beforeCommit");
    }
}
