package learn.java.testrepository.spring.request;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/reactive/webflux")
public class WebFluxController {

    @GetMapping("/mono")
    public Mono<String> mono() {
        log.info("spring Mono API started");

        final Mono<String> mono = Mono.just("hello")
            .doOnNext(log::info)
            .log()
            .map(val -> {
                someBlockingJob();
                return val + " mono";
            });

        log.info("spring Mono API terminated");
        return mono;
    }

    @GetMapping("/mono-async")
    public Mono<String> monoAsync() {
        log.info("spring Mono API started");

        final Mono<String> mono = Mono.fromCompletionStage(this::someAsyncJob)
            .doOnNext(log::info)
            .log()
            .map(val -> "hello " + val);

        log.info("spring Mono API terminated");
        return mono;
    }

    public CompletableFuture<String> someAsyncJob() {
        return CompletableFuture
            .supplyAsync(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "async";
            });
    }

    private void someBlockingJob() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
    }
}
