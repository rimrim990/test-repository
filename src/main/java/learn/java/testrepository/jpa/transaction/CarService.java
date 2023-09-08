package learn.java.testrepository.jpa.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public CarResponse getCar(final long id) {
        try {
            Car car = carRepository.findById(id);
            return new CarResponse(car.getId(), car.getName());
        } catch (CarException ex) {
            return null;
        }
    }
}
