package learn.java.testrepository.plain.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeErasureExample {

    // case1) 타입 파라미터가 여러 개 존재
    public <T, E> Map<T, E> genericMethod1(final T key, final E value) {
        return new HashMap<>();
    }

    // case2) 제네릭 배열
    public <T> T genericMethod2(final T[] genericArr) {
        return genericArr[0];
    }

    // case3) 비한정적 와일드카드
    public Object genericMethod3(final List<?> unboundedArr) {
        return unboundedArr.get(0);
    }

    // case3) 한정적 와일드카드
    public <T extends Comparable<T>> T genericMethod4(final T bounded) {
        return bounded;
    }

    // case4) 중첩
    public <T> List<List<T>> genericMethod45(final T elem) {
        return new ArrayList<>();
    }
}