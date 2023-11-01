package learn.java.testrepository.plain.async;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@DisplayName("자바 CompletableFuture 학습 테스트")
public class CompletableFutureTest {

    @Test
    @DisplayName("CompletableFuture.runAsync 로 비동기 작업 수행")
    void completableFuture_runAsync() throws ExecutionException, InterruptedException {
       // given
        Future<String> result = CompletableFuture.supplyAsync(() -> {
            // ForkJoin 풀을 사용하여 비동기 작업 처리
            log.info("run async");
            return "result";
        });

        log.info("exit");

        // when & then
        assertThat(result.get()).isEqualTo("result");
    }

    @Test
    @DisplayName("CompletableFuture.runAsyn 에 반환 값에 체이닝 추가")
    void completableFuture_chain() {
        CompletableFuture
            .runAsync(() -> log.info("run async"))
            // 동일한 스레드에서 수행
            .thenRun(() -> log.info("then run async"));

        log.info("exit");
    }

    @Test
    @DisplayName("CompletableFuture.supply 를 사용하여 비동기 작업의 반환 값 사용")
    void completableFuture_supply() {
        // 비동기 작업에 의존관계 설정
        CompletableFuture
            .supplyAsync(() -> {
                log.info("supplyApply");
                return "hello";
            })
            .thenApply(res -> {
                log.info("thenApply");
                return res + " bye";
            })
            .thenAccept(res -> log.info("thenAccept, {}", res));

        log.info("exit");
    }

    @Test
    @DisplayName("CompletableFuture.thenApplyAsync 로 다른 스레드에서 비동기 체이닝")
    void completableFuture_thenApplyAsync() {
        // given
        CompletableFuture
            .runAsync(() -> log.info("runAsync"))
            .thenRunAsync(() -> log.info("thenRunAsync"));
    }

    @Test
    @DisplayName("CompletableFuture.exceptionally 를 사용한 예외 처리")
    void completableFuture_exceptionally() throws ExecutionException, InterruptedException {
        // given
        final Future<String> result = CompletableFuture
            .supplyAsync(() -> {
                log.info("supplyAsync");
                throw new RuntimeException("async error occurred!");
            }).thenApplyAsync(res -> {
                // 예외 발생으로 스킵된다
                log.info("applyAsync");
                return res + " hello";
            }).exceptionally(e -> {
                // 예외를 처리한다
                log.error(e.getMessage());
                return "error";
            });

        // when & then
        assertThat(result.get()).isEqualTo("error");
    }

    @Test
    @DisplayName("CompletableFuture.handle 을 사용한 예외 처리")
    void completableFuture_handle() throws ExecutionException, InterruptedException {
        // given
        final Future<String> result = CompletableFuture
            .supplyAsync(() -> {
                log.info("supplyAsync");
                throw new RuntimeException("async error occurred!");
            }).thenApplyAsync(res -> {
                // 예외 발생으로 스킵된다
                log.info("applyAsync");
                return res + " hello";
            }).handle((s, ex) -> ex != null ? "error" : s);

        // when & then
        assertThat(result.get()).isEqualTo("error");
    }

    @Test
    @DisplayName("CompletableFuture.compose 로 CompletableFuture 결합하기")
    void completableFuture_compose() throws ExecutionException, InterruptedException {
        // given
        final CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return "hello";
        }).thenCompose(res ->
            CompletableFuture.supplyAsync(() -> {
                log.info("compose.supplyAsync");
                return res + " compose";
            })
        );

        // when & then
        assertThat(cf.get()).isEqualTo("hello compose");
    }
}
