package learn.java.testrepository.jpa.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jpa/transaction")
public class CarController {

    private final CarService carService;

    @GetMapping("/new")
    public void getCarWithNewTransaction() {
        carService.getWithNewTransactionAndLock(1L);
    }

    @GetMapping("/same")
    public void getCarWithSameTransaction() {
        carService.getWithSameTransactionAndLock(1L);
    }
}
