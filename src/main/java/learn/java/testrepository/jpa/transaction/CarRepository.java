package learn.java.testrepository.jpa.transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CarRepository {

    private final EntityManager em;

    public void save(final Car car) {
        em.persist(car);
        em.flush();
    }

    @Transactional(readOnly = true)
    public Car findById(final Long id) {
        try {
            return em.createQuery("SELECT c FROM Car c WHERE c.id = :id", Car.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch (NoResultException ex) {
            throw new CarException("no result", ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Car findByWithNewTransaction(final Long id) {
        return em.find(Car.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Car findByWithNewTransactionAndLock(final Long id) throws InterruptedException {
        Car car = em.createQuery("select c from Car c where c.id = :id", Car.class)
            .setParameter("id", id)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .getResultStream()
            .findFirst()
            .orElse(null);

        Thread.sleep(5000);
        return car;
    }

    @Transactional
    public Car findByWithSameTransaction(final Long id) {
        return em.find(Car.class, id);
    }

    @Transactional
    public Car findByWithSameTransactionAndLock(final Long id) throws InterruptedException {
        Car car = em.createQuery("select c from Car c where c.id = :id", Car.class)
            .setParameter("id", id)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .getResultStream()
            .findFirst()
            .orElse(null);

        Thread.sleep(5000);
        return car;
    }
}
