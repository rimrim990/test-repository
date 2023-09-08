package learn.java.testrepository.plain.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제네릭과 가변인수 함께 사용하는 테스트")
public class GenericWithVarargsTest {

    @DisplayName("타입 파라미터로 배열 선언은 원칙적으로 불가능하나, 가변인수를 사용하면 생성 가능하다")
    @Test
    void genericWithVarargs() {
        // when & then
        assertThat(test(1, 2, 3)).isInstanceOf(Integer[].class);
    }

    @DisplayName("제네릭과 가변인수를 함께 사용하면 런타임 타입 예외가 발생할 수 있다")
    @Test
    void genericWithVarargs_runtimeException() {
        // when & then
        assertThatThrownBy(() -> {
            Integer[] result = (Integer[]) test(1, 2, "hello");
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("잘못된 제네릭 사용으로 힙이 오염될 수 있다")
    @Test
    void heapPollution() {
        // given
        List<String> strList = new ArrayList<>();
        strList.add("hi");
        strList.add("hello");

        Object obj = strList;

        // when
        List<Double> doubleList = (List<Double>) obj; // 힙 오염
        doubleList.add(2.0); // ["hi", "hello", 2.0]

        // then
        assertThatThrownBy(() -> {
            for (Double value : doubleList) {
            }
        }).isInstanceOf(ClassCastException.class);
    }

    @DisplayName("checkList 는 힙 오염으로부터 안전하다")
    @Test
    void checkList() {
        // given
        List<String> strList = Collections.checkedList(new ArrayList<>(), String.class);
        strList.add("hi");
        strList.add("hello");

        Object obj = strList;
        List<Double> doubleList = (List<Double>) obj;

        // when
        assertThatThrownBy(() -> {
            doubleList.add(2.0);
        }).isInstanceOf(ClassCastException.class);
    }

    private <T> T[] test(final T... params) {
        return params;
    }
}
