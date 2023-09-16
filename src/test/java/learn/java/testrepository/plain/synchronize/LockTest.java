package learn.java.testrepository.plain.synchronize;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("뮤텍스와 세마포어 락 테스트")
public class LockTest {

    @Test
    @DisplayName("뮤텍스는 락을 획득한 스레드만 락을 해제할 수 있다")
    void mutex_releaseLock_onlyPermittedThread() {
        // given
        StringBuilder sharedSb = new StringBuilder();

        // when & then
        assertThatThrownBy(sharedSb::wait)
            .isInstanceOf(IllegalMonitorStateException.class);
    }

    @Test
    @DisplayName("세마포어는 락을 획득하지 않은 스레드도 시그널을 보낼 수 있다")
    void semaphore_releaseLock_anyThread() {
        // given
        Semaphore semaphore = new Semaphore(1);

        assertThatNoException()
            .isThrownBy(semaphore::release);
    }

    @Test
    @DisplayName("세마포어를 잘못 사용하면 허용 수보다 더 많은 스레드가 락을 획득할 수 있다")
    void semaphore() {
        // given
        int permitThread = 1;
        Semaphore semaphore = new Semaphore(permitThread);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // when
        for (int i = 0; i < 2; i++) {
            executorService.submit(() -> {
                try {
                    // 하나의 스레드만 접근할 수 있도록 설정했지만, release 를 잘못 호출하여 2개의 스레드가 진입
                    semaphore.acquire();
                    System.out.println(Thread.currentThread() + " acquired lock");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // then
        assertThatNoException()
            .isThrownBy(semaphore::release);
    }
}
