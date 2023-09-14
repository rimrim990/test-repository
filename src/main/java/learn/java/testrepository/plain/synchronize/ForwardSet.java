package learn.java.testrepository.plain.synchronize;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ForwardSet<E> {

    private final Set<E> s;

    public ForwardSet(Set<E> s) {
        this.s = s;
    }

    public int size() {
        return s.size();
    }

    public boolean isEmpty() {
        return s.isEmpty();
    }

    public boolean contains(E o) {
        return s.contains(o);
    }

    public Iterator<E> iterator() {
        return s.iterator();
    }

    public E[] toArray() {
        return (E[]) s.toArray();
    }


    public boolean add(E e) {
        return s.add(e);
    }

    public boolean remove(E o) {
        return s.remove(o);
    }

    public boolean containsAll(Collection<? extends E> c) {
        return s.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return s.addAll(c);
    }

    public boolean retainAll(Collection<? extends E> c) {
        return s.retainAll(c);
    }

    public boolean removeAll(Collection<? extends E> c) {
        return s.removeAll(c);
    }

    public void clear() {
        s.clear();
    }
}
