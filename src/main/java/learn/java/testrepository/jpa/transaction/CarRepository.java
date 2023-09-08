package learn.java.testrepository.jpa.transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CarRepository {

    private final EntityManager em;

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
}
