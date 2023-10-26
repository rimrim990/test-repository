package learn.java.testrepository.spring.reactive;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@DisplayName("스프링 @Async 학습 테스트")
public class AsyncTest {

    ApplicationContext ac;

    @BeforeEach
    void setUp() {
        ac = new AnnotationConfigApplicationContext(MyService.class, MyCallbackService.class);
    }

    @Test
    @DisplayName("@Async 비동기 작업의 반환 값으로 Future 를 지정할 수 있다")
    void async_returnFuture() throws InterruptedException, ExecutionException {
        // given
        final MyService myService = ac.getBean(MyService.class);

        // when
        final Future<String> result = myService.hello();

        // then
        assertThat(result.isDone()).isFalse();
        Thread.sleep(2000);
        assertThat(result.isDone()).isTrue();
        assertThat(result.get()).isEqualTo("hello");
    }

    @Test
    @DisplayName("@Async 비동기 작업에 콜백 메서드를 등록할 수 있다")
    void async_listenableFuture() throws InterruptedException {
        // given
        final MyCallbackService myCallbackService = ac.getBean(MyCallbackService.class);

        // when
        myCallbackService.hello();
        Thread.sleep(2000);
    }

    @Component
    private static class MyService {

        // 기본 Async Thread Pool - 매번 새로운 스레드를 생성한다
        @Async
        public Future<String> hello() {
            return CompletableFuture.supplyAsync(() -> {
                log.info("hello");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "hello";
            });
        }
    }

    @Component
    private static class MyCallbackService {

        @Async
        public Future<Void> hello() throws InterruptedException {

            return CompletableFuture.supplyAsync(() -> {
                log.info("hello");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "hello";
            }).thenAcceptAsync(s -> log.info("Result: " + s));
        }
    }
}
