package learn.java.testrepository.plain.synchronize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ObservableSet<E> extends ForwardSet<E> {

    private final List<SetObserver> observers = new ArrayList<>();

    public ObservableSet(Set<E> set) {
        super(set);
    }

    public boolean addObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }

    public void notifyElementAdded(E element) {
        synchronized (observers) {
            for (SetObserver<E> observer : observers) {
                observer.added(this, element);
            }
        }
    }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c) {
            result |= add(element);
        }
        return result;
    }
}

@FunctionalInterface
interface SetObserver<E> {
    void added(ObservableSet<E> set, E element);
}