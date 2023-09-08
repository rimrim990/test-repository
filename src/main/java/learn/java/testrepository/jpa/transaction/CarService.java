package learn.java.testrepository.jpa.transaction;

import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final EntityManager em;

    public CarResponse getCar(final long id) {
        try {
            Car car = carRepository.findById(id);
            return new CarResponse(car.getId(), car.getName());
        } catch (CarException ex) {
            return null;
        }
    }

    public void getWithSameTransaction(final long id) {
        carRepository.findByWithSameTransaction(id);
    }

    public void getWithSameTransactionAndLock(final long id) {
        try {
            carRepository.findByWithSameTransactionAndLock(1L);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void getWitNewTransaction(final long id) {
        carRepository.findByWithNewTransaction(id);
    }

    public void getWithNewTransactionAndLock(final long id) {
        try {
            carRepository.findByWithNewTransactionAndLock(1L);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
