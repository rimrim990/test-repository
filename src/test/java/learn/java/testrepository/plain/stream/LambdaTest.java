package learn.java.testrepository.plain.stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 람다 학습 테스트")
public class LambdaTest {

    @Test
    @DisplayName("@FunctionalInterface 선언으로 컴파일 타임에 체크가 가능하다")
    void functionInterface() {
        // when & then
        assertThatNoException()
            .isThrownBy(() -> {
                LambdaInterface lambda = () -> {};
            });
    }

    @Test
    @DisplayName("람다식에서 this 는 람다를 호출한 객체를 참조한다")
    void lambda_this() {
        // when
        final Supplier supplier = () -> this;

        // then
        assertThat(supplier.get())
            .isEqualTo(this);
    }

    @Test
    @DisplayName("Consumer 는 매개값은 있지만 반환값 없음")
    void consumer() {
        // given
        final Consumer<Integer> consumer = (a) -> System.out.println(a);

        // then
        assertThatNoException()
            .isThrownBy(() -> consumer.accept(3));
    }

    @Test
    @DisplayName("Supplier 는 매개값은 없지만 반환값 있음")
    void supplier() {
        // given
        final Supplier<Integer> supplier = () -> 3;

        // then
        assertThat(supplier.get()).isEqualTo(3);
    }

    @Test
    @DisplayName("Function 는 매개값과 반환값 모두 있음")
    void function() {
        // given
        final Function<Integer, String> function = (a) -> String.valueOf(a);

        // then
        assertThat(function.apply(3)).isEqualTo("3");
    }

    @Test
    @DisplayName("Operator 는 매개값과 반환값 모두 있음")
    void operator() {
        // given
        final IntBinaryOperator operator = (a, b) -> a * b;

        // then
        assertThat(operator.applyAsInt(3, 4)).isEqualTo(12);
    }

    @Test
    @DisplayName("Predicate 는 매개값과 반환값 모두 있음")
    void predicate() {
        // given
        final Predicate<Integer> predicate = (a) -> a == 3;

        // then
        assertThat(predicate.test(3)).isTrue();
    }


    @FunctionalInterface
    private interface LambdaInterface {
        void test();
    }
}
