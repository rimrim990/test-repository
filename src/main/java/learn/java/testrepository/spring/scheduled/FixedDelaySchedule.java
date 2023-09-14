package learn.java.testrepository.spring.scheduled;

import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FixedDelaySchedule {

    @Scheduled(fixedDelay = 50, timeUnit = TimeUnit.SECONDS, initialDelay = 10)
    void schedule() {
        System.out.println(Thread.currentThread() + "FixedDelaySchedule.schedule");
    }
}
