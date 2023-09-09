package learn.java.testrepository.spring.aop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class UserServiceTx implements UserService{

    private final EntityManager em;
    private final UserService userService;

    @Override
    public User createUser(String name) {
        final EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            final User user = userService.createUser(name);
            transaction.commit();
            return user;
        } catch (RuntimeException ex) {
            transaction.rollback();
            throw ex;
        }
    }
}
