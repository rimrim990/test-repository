package learn.java.testrepository.plain.generic;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("한정적 와일드카드 타입을 적용한 스택 API 테스트")
class StackTest {

    @Test
    @DisplayName("와일드카드가 없는 스택 - 동일한 타입 컬렉션으로만 pushAll 호출 가능하다")
    void stackWithoutWildCart_pushAll() {
        // given
        final Stack<Number> stack = new Stack<>();

        final List<Integer> intList = new ArrayList<>();
        final List<Number> numberList = new ArrayList<>();

        // when & then
        assertThatNoException().isThrownBy(() -> {
            //제네릭의 불공변 특성으로 인해 컴파일 에러 발생!
            //stack.pushAll(intList);

            stack.pushAll(numberList);
        });
    }

    @Test
    @DisplayName("와일드카드 스택 - 하위 타입을 상속한 컬렉션으로 pushAll 호출 가능하다")
    void stackWildCart_pushAll() {
        // given
        final WildCardStack<Number> stack = new WildCardStack<>();

        final List<Integer> intList = new ArrayList<>();
        final List<Number> numberList = new ArrayList<>();

        // when & then
        assertThatNoException().isThrownBy(() -> {
            // Number 를 상속받았기 때문에 컴파일 에러 X
            stack.pushAll(intList);
            stack.pushAll(numberList);
        });
    }

    @Test
    @DisplayName("와일드카드가 없는 스택 - 동일한 타입 컬렉션으로만 popAll 호출 가능하다")
    void stackWithoutWildCart_popAll() {
        // given
        final Stack<Number> stack = new Stack<>();
        stack.pushAll(List.of(1, 2, 3, 4, 5));

        final List<Object> objList = new ArrayList<>();
        final List<Number> numberList = new ArrayList<>();

        // when & then
        assertThatNoException().isThrownBy(() -> {
            //제네릭의 불공변 특성으로 인해 컴파일 에러 발생!
            //stack.popAll(objList);

            stack.popAll(numberList);
        });
    }

    @Test
    @DisplayName("와일드카드 스택 - 상위 타입 컬렉션으로 popAll 호출 가능하다")
    void stackWithWildCart_popAll() {
        // given
        final WildCardStack<Number> stack = new WildCardStack<>();
        stack.pushAll(List.of(1, 2, 3, 4, 5));

        final List<Object> objList = new ArrayList<>();
        final List<Number> numberList = new ArrayList<>();

        // when & then
        assertThatNoException().isThrownBy(() -> {
            stack.popAll(objList);
            stack.popAll(numberList);
        });
    }

    @Test
    @DisplayName("반환 값에 와일드카드를 사용하면 클라이언트 코드도 와일드카드를 사용하게 된다")
    void returnWildCardType() {
        // given
        final WildCardStack<Number> stack = new WildCardStack<>();
        stack.pushAll(List.of(1, 2, 3, 4, 5));

        // when & then
        assertThatNoException().isThrownBy(() -> {
            Set<? extends Number> numbers = stack.toSet();
        });
    }

    @Test
    @DisplayName("List<List<?>> 실험해보기")
    void nestedListWithUnboundWildcard_ver1() {
        // given
        List<List<?>> list = new ArrayList<>();

        // when
        list.add(new ArrayList<Integer>());
        list.add(new ArrayList<String>());
        list.add(new ArrayList<Number>());

        List<?> element = list.get(0);
        Object obj = element.get(0);
    }

    @Test
    @DisplayName("List<? extends List<?>> 실험해보기")
    void nestedListWithUnboundWildcard_ver2() {
        // given
        List<? extends List<?>> list = new ArrayList<>();
        List<List<Integer>> intList = new ArrayList<>();
        List<List<String>> strList = new ArrayList<>();
        List<List<Number>> numberList = new ArrayList<>();

        // when
        list.add(null);

        method(intList);
        method(strList);
        method(numberList);

        List<?> element = list.get(0);
        Object obj = element.get(0);
    }

    private void method(final List<? extends List<?>> list) {
    }
}