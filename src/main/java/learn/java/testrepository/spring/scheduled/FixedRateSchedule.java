package learn.java.testrepository.spring.scheduled;

import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FixedRateSchedule {

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void schedule() {
        System.out.println(Thread.currentThread() + "FixedRateSchedule.schedule");
    }
}
