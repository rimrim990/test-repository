package learn.java.testrepository.plain.async;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 비동기 작업 가져오기 -> Future, Callback
 */
@Slf4j
@DisplayName("자바 Future 학습 테스트")
public class FutureTest {

    @Test
    @DisplayName("Future 는 비동기 작업의 수행 결과를 가져올 수 있는 방법을 제공한다")
    void future() throws ExecutionException, InterruptedException {
        // given
        final ExecutorService es = Executors.newCachedThreadPool();

        // when
        final Future<String> result = es.submit(() -> {
            Thread.sleep(2000);
            log.info("hello");
            return "hello";
        });
        es.shutdown();

        // 비동기 작업이 수행되는 동안 메인 스레드에서 다른 작업 수행
        log.info("exit");

        // then
        assertThat(result.get()).isEqualTo("hello");
    }

    @Test
    @DisplayName("Future.isDone 으로 비동기 작업의 완료 여부를 알 수 있다")
    void future_isDone() throws ExecutionException, InterruptedException {
        // given
        final ExecutorService es = Executors.newCachedThreadPool();

        // when
        final Future<String> result = es.submit(() -> {
            Thread.sleep(2000);
            log.info("hello");
            return "hello";
        });
        es.shutdown();

        log.info("exit");

        // then
        assertThat(result.isDone()).isFalse();
        assertThat(result.get()).isEqualTo("hello");
        assertThat(result.isDone()).isTrue();


    }

    @Test
    @DisplayName("FutureTask 로 비동기 작업을 정의할 수 있다")
    void futureTask() throws ExecutionException, InterruptedException {
        // given
        final ExecutorService es = Executors.newCachedThreadPool();

        // when
        final FutureTask<String> future= new FutureTask<>(() -> {
            Thread.sleep(2000);
            log.info("hello");
            return "hello";
        });

        es.execute(future);
        es.shutdown();

        // 비동기 작업이 수행되는 동안 메인 스레드에서 다른 작업 수행
        log.info("exit");

        // then
        assertThat(future.get()).isEqualTo("hello");
    }

    @Test
    @DisplayName("FutureTask 익명 구현 객체를 생성하여 콜백 메서드 등록하기")
    void futureTask_callBack() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newCachedThreadPool();

        // when
        final FutureTask<String> future= new FutureTask<>(() -> {
            Thread.sleep(2000);
            return "hello";
        }) {
            @Override
            protected void done() {
                try {
                    log.info(get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(future);
        es.shutdown();

        log.info("exit");
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("FutureTask 를 상속하여 처리가 완료되었을 때 콜백을 수행하는 비동기 태스트 생성")
    void callbackFutureTask_success() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newCachedThreadPool();

        final CallbackFutureTask<String> cb = new CallbackFutureTask<>(() -> {
            Thread.sleep(2000);
            log.info("hello");
            return "hello";
        }, s -> System.out.println("Result: " + s), e -> System.out.println("Error: " + e));

        // when
        es.execute(cb);
        es.shutdown();

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("FutureTask 를 상속하여 에러가 발생했을 때 콜백을 수행하는 비동기 태스트 생성")
    void callbackFutureTask_error() throws InterruptedException {
        // given
        final ExecutorService es = Executors.newCachedThreadPool();

        final CallbackFutureTask<String> cb = new CallbackFutureTask<>(() -> {
            Thread.sleep(2000);
            throw new RuntimeException("Async Error Occurred!");
        }, s -> System.out.println("Result: " + s), e -> System.out.println("Error: " + e));

        // when
        es.execute(cb);
        es.shutdown();

        Thread.sleep(2000);
    }
}
