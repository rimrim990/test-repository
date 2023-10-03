package learn.java.testrepository.plain.synchronize;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThreadLocal 학습 테스트")
public class ThreadLocalTest {

    @Test
    @DisplayName("ThreadLocal 은 스레드 별로 값을 생성하고 보관한다")
    void threadLocal() throws ExecutionException, InterruptedException {
        // given
        final ThreadLocal<Integer> local = new ThreadLocal<>();
        local.set(1);

        // when
        final Integer otherThreadVar = Executors.newSingleThreadExecutor()
            .submit(() -> {
                local.set(2);
                return local.get();
            })
            .get();

        final Integer curThreadVar = local.get();

        // then
        assertThat(curThreadVar).isNotEqualTo(otherThreadVar);
    }

    @Test
    @DisplayName("톰캣은 스레드 풀로 스레드를 재사용하므로 스레드 로컬 값을 반드시 초기화해줘야 한다")
    void threadLocal_remove() {
        // given
        final ThreadLocal<Integer> local = new ThreadLocal<>();

        // when
        local.set(10);
        local.remove();

        // then
        assertThat(local.get()).isNull();
    }
}
