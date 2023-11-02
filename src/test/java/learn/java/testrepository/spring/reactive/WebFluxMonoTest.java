package learn.java.testrepository.spring.reactive;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import learn.java.testrepository.config.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

@Slf4j
@DisplayName("스프링 웹 플럭스 Mono 학습 테스트")
public class WebFluxMonoTest extends IntegrationTest {

    @Test
    @DisplayName("Mono.just 로 Mono 를 생성한다")
    void mono_just() {
        // subscribe 를 수행하지 않으면 요청은 처리되지 않는다
        Mono.just("hello")
            .doOnNext(val -> log.info("doOnNext: {}", val))
            .subscribe(val -> log.info("subscribe: {}", val));
    }

    @Test
    @DisplayName("Mono.just 에 등록한 메서드는 블로킹 방식으로 동작한다")
    void mono_just_blocking() {
        // given
        final StopWatch sw = new StopWatch();
        log.info("pos1");

        // when
        sw.start();
        final Mono<String> mono = Mono.just(someBlockingJob("hello"));
        log.info("pos2");
        mono.subscribe(log::info);

        // then
        sw.stop();
        assertThat(sw.getTotalTimeSeconds()).isGreaterThan(1);
    }

    @Test
    @DisplayName("Mono 를 사용하여 비동기 작업을 처리할 수 있다")
    void mono_async() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newSingleThreadExecutor();

        // when
        log.info("pos1");

        es.submit(() -> {
            Mono.just("hello")
                .log()
                .map(this::someBlockingJob)
                .subscribe(val -> log.info(val + " flux"));
        });

        // then
        log.info("pos2");

        es.awaitTermination(2, TimeUnit.SECONDS);
        es.shutdown();
    }

    @Test
    @DisplayName("Mono 는 여러 번 구독하더라도 동일한 결과를 반환한다")
    void mono_subscribe() {
        // given
        final Mono<String> mono = Mono.just("hello")
            .doOnNext(val -> log.info("hello"))
            .map(val -> val + " spring");

        // when
        // 여러 번 구독하더라도 동일한 과정으로 데이터를 반환한다
        mono.subscribe(log::info);
        mono.subscribe(log::info);
    }

    @Test
    @DisplayName("Mono.block 을 호출하여 블로킹 방식으로 결과 값을 가져올 수 있다")
    void mono_block() {
        // given
        final Mono<String> mono = Mono.just("hello")
            .doOnNext(val -> log.info("hello"))
            .map(val -> val + " spring");

        // when
        // 블로킹 방식으로 Mono 를 실행한다
        log.info("block");
        final String result = mono.block();

        // then
        log.info("terminate");
        assertThat(result).isEqualTo("hello spring");
    }

    @Test
    @DisplayName("스프링은 Mono 반환 타입을 지원한다")
    void mono_spring() {
        // when
        final String result = RestAssured
            .get("/reactive/webflux/mono")
            .getBody()
            .asString();

        // when
        assertThat(result).isEqualTo("hello mono");
    }

    @Test
    @DisplayName("스프링 Mono 와 자바 CompletableFuture 를 결합하여 비동기 작업 처리가 가능하다")
    void mono_springAsync() {
        // when
        final String result = RestAssured
            .get("/reactive/webflux/mono-async")
            .getBody()
            .asString();

        // when
        assertThat(result).isEqualTo("hello async");
    }

    private String someBlockingJob(final String val) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return val + " spring";
    }
}

