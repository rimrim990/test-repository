package learn.java.testrepository.plain.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("클래스 리터럴을 키 값으로 하여 타입에 안전한 컨테이너를 만들 수 있다")
class TypeContainerTest {

    @Test
    @DisplayName("타입 컨테이너 테스트")
    void typeContainer() {
        // given
        final TypeContainer container = new TypeContainer();
        final String strValue = "test";

        // when
        container.put(String.class, strValue);

        // then
        assertThat(container.get(String.class)).isEqualTo(strValue);
    }

    @Test
    @DisplayName("타입 컨테이너는 일반적인 Map 과 다르게 여러 타입의 원소를 저장할 수 있다")
    void typeContainer_various_type() {
        // given
        final TypeContainer container = new TypeContainer();
        final Integer intValue = 1;
        final String strValue = "test";

        // when
        container.put(Integer.class, intValue);
        container.put(String.class, strValue);

        // then
        assertThat(container.get(String.class)).isEqualTo(strValue);
        assertThat(container.get(Integer.class)).isEqualTo(intValue);
    }

    @Test
    @DisplayName("Map 의 키 값이 비한정적 와일드카드 타입이었다면 값을 넣지 못한다")
    void unbounded_wildCard_map() {
        // given
        final Map<? , Integer> wildCardMap = new HashMap<>();

        // when & then
        assertThatNoException().isThrownBy(() -> wildCardMap.put(null, 1));
        // 컴파일 에러
        // wildCardMap.put("test", 1);
    }

    @Test
    @DisplayName("악의적인 사용자가 로 타입 클래 리터럴 값을 넣으면 ClassCastException 예외 발생")
    void invalid_class_literal() {
        // given
        final TypeContainer container = new TypeContainer();

        // when & then
        assertThatThrownBy(() -> container.put((Class) Integer.class, "invalid class"))
            .isInstanceOf(ClassCastException.class);
    }

    @Test
    @DisplayName("asSubClass 는 호출한 인스턴스의 Class 객체를 인수 클래스로 형변환한다")
    void asSubClass_callerIsSubClass_success() {
        // given
        final TypeContainer container = new TypeContainer();
        Class<?> type = String.class;

        // when & then
        assertThatNoException()
            .isThrownBy(() -> container.put(type.asSubclass(String.class), "test"));
    }

    @Test
    @DisplayName("asSubClass 는 호출한 인스턴스가 인수 클래스의 자식 클래스가 아니면 ClassCastException 예외 발생")
    void asSubClass_callerIsNotSubClass_throwsClassCastException() {
        // given
        Class<?> type = String.class;

        // when & then
        assertThatThrownBy(() -> type.asSubclass(Integer.class))
            .isInstanceOf(ClassCastException.class);
    }
}