package learn.java.testrepository.spring.request;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import learn.java.testrepository.config.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StopWatch;

@Slf4j
@DisplayName("스프링 Callable 비동기 API 학습 테스트")
@Import({TaskExecutorConfig.class})
@TestPropertySource(properties = {"server.tomcat.threads.max=20"})
class AsyncControllerTest extends IntegrationTest {

    @Test
    @DisplayName("서블릿 스레드가 최대 20개일 때 동기 API 의 수행 시간 측정")
    void sync_loadTest() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final AtomicInteger counter = new AtomicInteger();

        final StopWatch time = new StopWatch();
        time.start();

        // when
        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                request("/sync");

                sw.stop();
                log.info("Thread No.{} Elapsed: {}", idx, sw.getTotalTimeSeconds());
            });
        }

        // then
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop();
        log.info("Total: {}", time.getTotalTimeSeconds());

        assertThat(time.getTotalTimeSeconds()).isGreaterThan(10);
    }

    @Test
    @DisplayName("서블릿 스레드가 최대 20개일 때 비동기 API 의 수행 시간 측정")
    void async_loadTest() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final AtomicInteger counter = new AtomicInteger();

        final StopWatch time = new StopWatch();
        time.start();

        // when
        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                request("/async");

                sw.stop();
                log.info("Thread No.{} Elapsed: {}", idx, sw.getTotalTimeSeconds());
            });
        }

        // then
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop();
        log.info("Total: {}", time.getTotalTimeSeconds());

        assertThat(time.getTotalTimeSeconds()).isLessThan(10);
    }

    private void request(final String url) {
        RestAssured
            .given()
            .when()
            .get(url);
    }
}