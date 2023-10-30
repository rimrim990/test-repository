package learn.java.testrepository.spring.request;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
public class DeferredController {

    private final Queue<DeferredResult<String>> result = new ConcurrentLinkedQueue<>();

    @GetMapping("/dr")
    public DeferredResult<String> callable() {
        log.info("dr");
        DeferredResult<String> dr = new DeferredResult<>(60000L);
        result.add(dr);
        return dr;
    }

    @GetMapping("/dr/count")
    public DrResult<Integer> drcount() {
        final DrResult<Integer> drResult = new DrResult<>();
        drResult.setValue(result.size());
        return drResult;
    }

    @GetMapping("/dr/event")
    public DrResult<String> event() {
        for (final DeferredResult<String> dr : result) {
            dr.setResult("Hello");
            result.remove(dr);
        }

        final DrResult<String> drResult = new DrResult<>();
        drResult.setValue("OK");
        return drResult;
    }
}
