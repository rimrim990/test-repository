package learn.java.testrepository.plain.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Stack<E> {

    private final List<E> stack = new ArrayList<>();

    public void pushAll(final Iterable<E> elements) {
        for (final E element : elements) {
            stack.add(element);
        }
    }

    public void popAll(final Collection<E> destination) {
        for (final E element : stack) {
            destination.add(element);
        }
    }
}