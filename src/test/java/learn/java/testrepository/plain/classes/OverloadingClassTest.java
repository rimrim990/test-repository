package learn.java.testrepository.plain.classes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메서드 오버로딩 학습 테스트")
class OverloadingClassTest {



    @Test
    @DisplayName("메서드 오버로딩은 컴파일 타임 타입으로 메서드를 결정한다")
    void overloading_compileType() {
        // given
        final OverloadingClass overloadingClass = new OverloadingClass();

        Collection<?>[] collection = {
            new HashSet<String>(),
            new ArrayList<Integer>(),
            new HashMap<String, String>().values()
        };

        // when
        List<String> result = Arrays.stream(collection)
            .map(overloadingClass::method)
            .toList();

        // then
        assertThat(result).containsExactly("collection", "collection", "collection");
    }

    @Test
    @DisplayName("메서드 오버라이딩은 런타임 인스턴스 타입에 의해 결정된다")
    void overriding_runtime() {
        // given
        List<SuperClass> collection = List.of(
            new SuperClass(),
            new OverridingClass(),
            new OverridingClass()
        );

        // when
        List<String> result = collection.stream()
            .map(SuperClass::method)
            .toList();

        // then
        assertThat(result).containsExactly("super", "sub", "sub");
    }

    @Test
    @DisplayName("instanceof 를 사용하여 런타임 타입에 따른 결과 반환이 가능하다")
    void overloading_instanceof() {
        // given
        final OverloadingClass overloadingClass = new OverloadingClass();

        Collection<?>[] collection = {
            new HashSet<String>(),
            new ArrayList<Integer>(),
            new HashMap<String, String>().values()
        };

        // when
        List<String> result = Arrays.stream(collection)
            .map(overloadingClass::classify)
            .toList();

        // then
        assertThat(result).containsExactly("set", "list", "collection");
    }

    @Test
    @DisplayName("")
    void compileRuntimeMix() {
        // given
        OverridingClass sub = new OverridingClass();
        SuperClass sup = sub;

        // when & then
        assertThat(sub.test(sub)).isEqualTo("sub&sub");
        assertThat(sub.test(sup)).isEqualTo("sub&super");
        assertThat(sub.test((OverridingClass) sup)).isEqualTo("sub&sub");
        assertThat(sub.test((SuperClass) sub)).isEqualTo("sub&super");

        assertThat(sup.test(sub)).isEqualTo("sub&super");
        assertThat(sup.test(sup)).isEqualTo("sub&super");
        assertThat(sup.test((OverridingClass) sup)).isEqualTo("sub&super");
        assertThat(sup.test((SuperClass) sup)).isEqualTo("sub&super");
    }
}