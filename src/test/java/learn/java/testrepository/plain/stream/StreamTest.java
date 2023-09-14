package learn.java.testrepository.plain.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 스트림 테스트")
public class StreamTest {

    @Test
    @DisplayName("(지연 연산) 스트림 연산은 최종 연산을 수행하기 전까지 실행되지 않는다")
    void lazyEvaluation() {
        // given
        IntStream.of(1, 2, 3).map(this::process);
    }

    @Test
    @DisplayName("(지연 연산) 스트림 연산은 최종 연산이 수행되어야 실행된다")
    void lazyEvaluation_untilValueUsed() {
        // given
        IntStream stream = IntStream.of(1, 2, 3)
            .map(this::process);

        // when
        assertThat(stream.sum()).isEqualTo(6L);
    }

    @Test
    @DisplayName("(루프 병합) 스트림은 체이닝된 연산을 하나의 연산으로 병합하여 실행한다")
    void loopMerge() {
        // when
        IntStream.of(1, 2, 3)
            .map(this::process)
            .map(this::print)
            .sum();
    }

    @Test
    @DisplayName("(쇼트 서킷) 스트림은 불필요한 연산을 스킵한다")
    void shortCircuit() {
        // when
        IntStream.of(1, 2, 3)
            .map(this::process)
            .map(this::print)
            .limit(1)
            .sum();
    }

    @Test
    @DisplayName("(쇼트 서킷) sorted 가 사용되면 쇼트 서킷이 불가능하다")
    void shortCircuit_notAvailable() {
        // when
        IntStream.of(1, 2, 3)
            .map(this::process)
            .map(this::print)
            .sorted()
            .limit(1)
            .sum();
    }

    @Test
    @DisplayName("(병렬 처리) 병렬 스트림은 데이터를 분할하여 멀티 스레드에서 수행한다")
    void parallelStream() {
        // when
        IntStream.of(1, 2, 3, 4, 5)
            .parallel()
            .map(this::process)
            .map(this::print)
            .sum();
    }

    private Integer process(final Integer value) {
        System.out.println(Thread.currentThread() + " process called: " + value);
        return value;
    }

    private Integer print(final Integer value) {
        System.out.println(Thread.currentThread() +" print called: " + value);
        return value;
    }
}
