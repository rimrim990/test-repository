package learn.java.testrepository.plain.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("컬렉션 프레임워크와 제네릭 타입 테스트")
public class CollectionGenericTest {

    @DisplayName("제네릭을 사용하지 않으면 컬렉션에서 객체를 꺼낼 때 수동으로 형변환을 해줘야 한다")
    @Test
    void withoutGeneric_manualCasting() {
        // given
        List type = new ArrayList();
        type.add("test");
        type.add(Integer.valueOf(1));

        // then & then
      assertThatNoException().isThrownBy(() -> {
          // Object 타입이므로 명시적인 형 변환이 필요하다
          String strVal = (String) type.get(0);
          Integer intVal = (Integer) type.get(1);
      });
    }

    @DisplayName("제네릭을 사용하면 컬렉션에서 객체를 꺼낼 때 형변환을 자동 제공한다")
    @Test
    void withGeneric_autoCasting() {
        // given
        List<String> genericType = new ArrayList<>();
        genericType.add("test");

        // when & then
        assertThatNoException().isThrownBy(() -> {
            String strVal = genericType.get(0);
        });
    }

    @DisplayName("제네릭을 사용하지 않으면 런타임에 형변환 예외가 발생할 수 있다")
    @Test
    void withoutGeneric_catchCastingException_atRunTime() {
        // given
        List type = new ArrayList();

        // when
        type.add(Integer.valueOf(2));

        // then
        assertThatThrownBy(() -> {
            String s = (String) type.get(0);
        }).isInstanceOf(ClassCastException.class);
    }

    @DisplayName("제네릭을 사용하면 제네릭의 하위 타입 규칙에 타입 안전성을 보장받을 수 있다")
    @Test
    void withGeneric_assureTypeSafe() {
        // given
        List<Object> objectList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        // List<Object> 와 List<String> 은 호환되지 않으므로 컴파일 에러가 발생한다
        // objectList = stringList;
    }

    @DisplayName("비한정적 와일드카드 타입은 어떤 타입이라도 담을 수 있다")
    @Test
    void unboundedWildCardType() {
        // given
        List<String> stringList = new ArrayList<>();
        stringList.add("test");

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);

        // when & then
        assertThat(wildCardMethod(stringList)).isInstanceOf(Object.class);
        assertThat(wildCardMethod(integerList)).isInstanceOf(Object.class);
    }

    @DisplayName("비한정적 와일드카드 타입에는 null 이외의 어떤 값도 넣을 수 없다")
    @Test
    void unboundedWildCardType_canAddOnlyNull() {
        // given
        List<?> wildCardList = new ArrayList<>();

        // when & then
        assertThatNoException().isThrownBy(() -> wildCardList.add(null));
        // 컴파일 에러가 발생한다
        // wildCardList.add("String");
    }

    @DisplayName("클래스 리터럴에는 로 타입만 사용 가능하다")
    @Test
    void classLiteral_onlyRawType() {
        // when & then
        assertThatNoException().isThrownBy(() -> {
            // 로 타입만 허용
            Class<List> listClass = List.class;
            // 컴파일 에러 !
            // Class<List<Integer>> listGenericClass = List<Integer>.class;
        });
    }

    private Object wildCardMethod(List<?> list) {
        // 1. null 이외의 데이터 추가는 불가능하다
        // list.add("test");

        // 2. Object 이외의 타입으로 형변환은 불가능하다
        // String val = list.get(0);

        // 데이터를 Object 타입으로 꺼내올 수만 있다
        return list.get(0);
    }
}
