package learn.java.testrepository.plain.generic;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제네릭의 불공변 속성 테스트")
public class GenericInvarianceTest {

    @Test
    @DisplayName("제네릭과 다르게 배열은 공변이다")
    void array_Variant() {
        // given
        final String[] strArr = new String[2];

        // when & then
        assertThatNoException().isThrownBy(() -> {
            final Object[] objArr = strArr;
        });
    }

    @Test
    @DisplayName("배열의 공변 속성은 런타임 에러를 발생시킬 수 있다")
    void array_Variant_RuntimeException() {
        // given
        final Object[] objArr = new Long[2];
        objArr[0] = 1L;

        // when & then
        assertThatThrownBy(() -> {
            objArr[1] = "type mismatch !!";
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("제네릭은 불공변이다")
    void generic_Invariant() {
        // given
        List<Object> objList = new ArrayList<>();
        List<String> strList = new ArrayList<>();

        // when & then
        // 컴파일 에러 발생 !
        // objList = strList;
    }

    @Test
    @DisplayName("제네릭 배열은 생성될 수 없다")
    void arrayOfGeneric_CompileError() {
        // 1. 컴파일 에러 - "제네릭 배열 생성"
        // List<String>[] test1 = new List<String>[2];

        // 2. 컴파일 에러 - "타입 매개변수 E 를 직접 인스턴스화 할 수 없음"
        // E[] test2 = new E[2];
    }

    @Test
    @DisplayName("배열을 타입 파라미터로 갖는 제네릭은 생성될 수 있다")
    void genericOfArray_IsPossible() {
        // given
        List<String[]> strArrList = new ArrayList<>();
        String[] strArr = new String[]{"hi", "hello"};

        // when
        strArrList.add(strArr);

        // then
        assertThat(strArrList.get(0)).isEqualTo(strArr);
    }

    @Test
    @DisplayName("배열과 제네릭을 혼용하면 컴파일 타임에 타입 예외를 발견할 수 없다")
    void genericOfArray_RuntimeException() {
        // given
        List<Object[]> objArrList = new ArrayList<>();
        Long[] longArr = new Long[]{1L, 2L};
        String[] strArr = new String[]{"hi", "hello"};

        // when
        objArrList.add(longArr);
        objArrList.add(strArr);

        // then
        assertThatThrownBy(() -> {
            String str = (String) objArrList.get(0)[0];
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("제네릭 타입은 실체화되지 않는다 - 런타임에는 타입 정보가 제거된다")
    void generic_reify() {
        // given
        List<String> strListParam = new ArrayList<>();
        List<Integer> intListParam = new ArrayList<>();

        // when & then
        assertThat(getRuntimeClassName(strListParam)).isEqualTo(ArrayList.class.getName()); // ArrayList
        assertThat(getRuntimeClassName(intListParam)).isEqualTo(ArrayList.class.getName()); // ArrayList
        assertThat(getRuntimeClassName(strListParam)).isEqualTo(getRuntimeClassName(intListParam));
    }

    @Test
    @DisplayName("배열 타입은 실체화된다 - 런타임에도 타입 정보를 유지한다")
    void array_reify() {
        // given
        String[] strArrParam = new String[]{"hi", "hello"};
        Integer[] intArrParam = new Integer[]{1, 2};

        // when & then
        assertThat(getRuntimeClassName(strArrParam)).isEqualTo(String[].class.getName()); // String[]
        assertThat(getRuntimeClassName(intArrParam)).isEqualTo(Integer[].class.getName()); // Integer[]
        assertThat(getRuntimeClassName(strArrParam)).isNotEqualTo(getRuntimeClassName(intArrParam));
    }

    private <E> String getRuntimeClassName(final E param) {
        // Object 의 런타임 클래스 이름 반환
        return param.getClass().getName();
    }
}
