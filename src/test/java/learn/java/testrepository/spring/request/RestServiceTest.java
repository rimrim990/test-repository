package learn.java.testrepository.spring.request;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

@Slf4j
@DisplayName("비동기 API 호출 학습 테스트")
public class RestServiceTest {

    @Test
    @DisplayName("RestTemplate 동기 API 부하 테스트")
    void sync_loadTest() throws BrokenBarrierException, InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final AtomicInteger counter = new AtomicInteger();
        final StopWatch time = new StopWatch();
        final CyclicBarrier barrier = new CyclicBarrier(101);

        // when
        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);
                barrier.await(); // await 이 100번 수행될 때까지 블락킹

                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String result = request("http://localhost:8080/rest?idx=" + idx);

                sw.stop();
                log.info("Elapsed: {}, {}", sw.getTotalTimeSeconds(), result);

                return null;
            });
        }

        // then
        barrier.await();
        time.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop(); // 100개 API 요청을 처리하는 시간 측정
        log.info("Total Elapsed: ", time.getTotalTimeSeconds());
    }

    @Test
    @DisplayName("WebClient 비동기 API 부하 테스트")
    void async_loadTest() throws BrokenBarrierException, InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final AtomicInteger counter = new AtomicInteger();
        final StopWatch time = new StopWatch();
        final CyclicBarrier barrier = new CyclicBarrier(101);

        // when
        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);
                barrier.await(); // await 이 100번 수행될 때까지 블락킹

                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String result = request("http://localhost:8080/rest-async?idx=" + idx);

                sw.stop();
                log.info("Elapsed: {}, {}", sw.getTotalTimeSeconds(), result);

                return null;
            });
        }

        // then
        barrier.await();
        time.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop(); // 100개 API 요청을 처리하는 시간 측정
        log.info("Total Elapsed: {}", time.getTotalTimeSeconds());

        assertThat(time.getTotalTimeSeconds()).isLessThan(10);
    }

    @Test
    @DisplayName("WebClient 비동기 콜백 API 부하 테스트")
    void asyncCallback_loadTest() throws BrokenBarrierException, InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final AtomicInteger counter = new AtomicInteger();
        final StopWatch time = new StopWatch();
        final CyclicBarrier barrier = new CyclicBarrier(101);

        // when
        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);
                barrier.await(); // await 이 100번 수행될 때까지 블락킹

                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String result = request("http://localhost:8080/rest-async-callback?idx=" + idx);

                sw.stop();
                log.info("Elapsed: {}, {}", sw.getTotalTimeSeconds(), result);

                return null;
            });
        }

        // then
        barrier.await();
        time.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop(); // 100개 API 요청을 처리하는 시간 측정
        log.info("Total Elapsed: {}", time.getTotalTimeSeconds());

        assertThat(time.getTotalTimeSeconds()).isLessThan(10);
    }

    @Test
    @DisplayName("WebClient 비동기 콜백 체인 API 부하 테스트")
    void asyncCallbackChain_loadTest() throws BrokenBarrierException, InterruptedException {
        // given
        final ExecutorService es = Executors.newFixedThreadPool(100);
        final AtomicInteger counter = new AtomicInteger();
        final StopWatch time = new StopWatch();
        final CyclicBarrier barrier = new CyclicBarrier(101);

        // when
        for (int i = 0; i < 100; i++) {
            es.submit(() -> {
                int idx = counter.addAndGet(1);
                barrier.await(); // await 이 100번 수행될 때까지 블락킹

                log.info("Thread No.{}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String result = request("http://localhost:8080/rest-async-chain?idx=" + idx);

                sw.stop();
                log.info("Elapsed: {}, {}", sw.getTotalTimeSeconds(), result);

                return null;
            });
        }

        // then
        barrier.await();
        time.start();

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        time.stop(); // 100개 API 요청을 처리하는 시간 측정
        log.info("Total Elapsed: {}", time.getTotalTimeSeconds());

        assertThat(time.getTotalTimeSeconds()).isLessThan(10);
    }

    private String request(final String url) {
        return RestAssured
            .given()
            .when()
            .get(url)
            .then()
            .contentType(ContentType.TEXT)
            .extract()
            .asString();
    }
}
