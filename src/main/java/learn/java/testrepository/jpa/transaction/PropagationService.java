package learn.java.testrepository.jpa.transaction;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class PropagationService {

    private final EntityManager entityManager;

    /**
     * 기존 트랜잭션에 참가한다 - REQUIRED, SUPPORTS, MANDATORY
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String required() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public String supports() {
        entityManager.persist(new Car("test"));
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public String mandatory() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    /**
     * 트랜잭션을 사용하지 않는다
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String notSupported() {
        entityManager.persist(new Car("test"));
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.NEVER)
    public String never() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    /**
     * 트랜잭션을 새로 생성한다
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String requiresNew() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }

    @Transactional(propagation = Propagation.NESTED)
    public String nested() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
