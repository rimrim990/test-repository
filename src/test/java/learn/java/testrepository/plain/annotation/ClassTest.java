package learn.java.testrepository.plain.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Class 클래스 테스트")
public class ClassTest {

    @Test
    @DisplayName("IsInstance 메서드를 동일한 클래스 인스턴스에 호출하면 true 를 반환한다")
    void isInstance_sameClass_returnTrue() {
        // given
        final Class<String> clazz = String.class;

        // when & then
        assertThat(clazz.isInstance("test")).isTrue();
    }

    @Test
    @DisplayName("IsInstance 메서드를 부모 클래스 인스턴스에 호출하면 false 를 반환한다")
    void isInstance_superClass_returnTrue() {
        // given
        final Class<String> clazz = String.class;

        // when & then
        assertThat(clazz.isInstance(new Object())).isFalse();
    }

    @Test
    @DisplayName("IsInstance 메서드를 하위 클래스 인스턴스에 호출하면 true 를 반환한다")
    void isInstance_subClass_returnFalse() {
        // given
        final Class<Object> clazz = Object.class;

        // when & then
        assertThat(clazz.isInstance("test")).isTrue();
    }
}
