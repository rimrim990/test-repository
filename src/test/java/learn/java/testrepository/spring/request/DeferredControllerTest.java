package learn.java.testrepository.spring.request;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import learn.java.testrepository.config.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StopWatch;

@Slf4j
@DisplayName("스프링 DeferredResult 비동기 API 학습 테스트")
@TestPropertySource(properties = {"server.tomcat.threads.max=20"})
class DeferredControllerTest extends IntegrationTest {

    @Test
    @DisplayName("별도의 워커 스레드를 생성하지 않고 비동기 API 를 실행한다")
    void deferred_withoutWorkerThread() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(101);
        final AtomicInteger counter = new AtomicInteger();
        final AtomicInteger beforeEventCount = new AtomicInteger();

        final StopWatch time = new StopWatch();
        time.start();

        // when
        for (int i = 0; i < 100; i++) {
            es.execute(() -> {
                int idx = counter.addAndGet(1);
                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                request("/dr");

                sw.stop();
                log.info("Thread No.{} Elapsed: {}", idx, sw.getTotalTimeSeconds());
            });
        }

        es.execute(() -> {
            try {
                Thread.sleep(2000);

                DrResult<Integer> result = requestInfo("/dr/count");
                beforeEventCount.set(result.getValue());

                requestInfo("/dr/event");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // then
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop();
        log.info("Total: {}", time.getTotalTimeSeconds());

        DrResult<Integer> result =  requestInfo("/dr/count");
        Integer afterEventCount = result.getValue();

        assertThat(beforeEventCount.get()).isEqualTo(100);
        assertThat(afterEventCount).isEqualTo(0);
    }

    private DrResult requestInfo(final String url) {
        return RestAssured
            .given()
            .when()
            .get(url)
            .then()
            .contentType(ContentType.JSON)
            .extract()
            .as(DrResult.class);
    }

    private void request(final String url) {
        RestAssured
            .given()
            .when()
            .get(url);
    }
}