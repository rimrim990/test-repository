package learn.java.testrepository.plain.generic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TypeContainer {

    private final Map<Class<?>, Object> values = new HashMap<>();

    public <T> void put(final Class<T> type, final T value) {
        // 로 타입 넣는 것을 방지
        values.put(Objects.requireNonNull(type), type.cast(value));
    }

    public <T> T get(final Class<T> clazz) {
        // Casts an object to the class or interface represented by this Class object.
        // throws ClassCastException
        return clazz.cast(values.get(clazz));
    }
}
