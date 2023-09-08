package learn.java.testrepository.plain.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class WildCardStack<E> {

    private final List<E> stack = new ArrayList<>();

    public void pushAll(final Iterable<? extends E> elements) {
        for (final E element : elements) {
            stack.add(element);
        }
    }

    public void popAll(final Collection<? super E> destination) {
        for (final E element : stack) {
            destination.add(element);
        }
    }

    // 반환 값에 불필요한 와일드카드 사용
    public Set<? extends E> toSet() {
        return new HashSet<>();
    }
}
