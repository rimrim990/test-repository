package learn.java.testrepository.spring.request;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AsyncController {

    /**
     * I/O가 동기적으로 처리되는 API
     */
    @GetMapping("/sync")
    public String sync() throws InterruptedException {
        log.info("sync");
        Thread.sleep(2000);
        return "hello";
    }

    /**
     * Spring MVC 는 Callable 반환 타입을 처리해준다
     */
    @GetMapping("/async")
    public Callable<String> async() {
        log.info("callable");
        // 별도의 스레드에서 수행된다
        return () -> {
            log.info("async");
            Thread.sleep(2000);
            return "hello";
        };
    }
}
