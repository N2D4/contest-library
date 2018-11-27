package lib.utils.various;

import java.util.*;

public abstract class AbstractSortedSet<T> extends AbstractSet<T> implements SortedSet<T> {
    @Override
    public abstract Iterator<T> iterator();

    @Override
    public abstract boolean contains(Object obj);

    @Override
    public abstract boolean add(T t);

    @Override
    public abstract boolean remove(Object t);

    @Override
    public abstract int size();

    @Override
    public abstract Comparator<? super T> comparator();







    @Override
    public T first() {
        return iterator().next();
    }

    @Override
    public T last() {
        Iterator<T> iterator = iterator();
        T last = iterator.next();
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return last;
    }





    protected SortedSet<T> subSet(T fromElement, boolean recFrom, T toElement, boolean recTo) {
        AbstractSortedSet<T> outer = this;
        return new AbstractSortedSet<T>() {

            private boolean isInRange(Object obj) {
                Comparator<? super T> cmp = comparator();
                if (cmp == null) {
                    return ((Comparable<? super T>) obj).compareTo(fromElement) >= 0 &&
                           ((Comparable<? super T>) obj).compareTo(toElement) < 0;
                } else {
                    return recFrom && Objects.compare(fromElement, (T) obj, cmp) <= 0 &&
                           recTo && Objects.compare(toElement, (T) obj, cmp) > 0;
                }
            }

            @Override
            public Iterator<T> iterator() {
                return FunctionalIterators.filter(outer.iterator(), this::isInRange);
            }

            @Override
            public boolean contains(Object obj) {
                return isInRange(obj) && outer.contains(obj);
            }

            @Override
            public boolean add(T t) {
                if (!isInRange(t)) throw new IllegalArgumentException();
                return outer.add(t);
            }

            @Override
            public boolean remove(Object t) {
                if (!isInRange(t)) return false;
                return outer.remove(t);
            }

            @Override
            public int size() {
                int size = 0;
                Iterator<T> iterator = iterator();
                while (iterator.hasNext()) {
                    size++;
                    iterator.next();
                }
                return size;
            }

            @Override
            public Comparator<? super T> comparator() {
                return outer.comparator();
            }

            @Override
            public T first() {
                if (recFrom && contains(fromElement)) return fromElement;
                return super.first();
            }
        };
    }


    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, true);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return subSet(null, false, toElement, true);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return subSet(fromElement, true, null, false);
    }



}
