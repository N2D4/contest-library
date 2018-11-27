package lib.utils.various;

import lib.polyfill.PolyfillIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


public final class FunctionalIterators {
    private FunctionalIterators() {
        // Quite dusty here...
    }

    public static <T> Iterator<T> filter(Iterator<T> iterator, Predicate<T> filter) {
        return new SelectiveIterator<T>(iterator, filter);
    }

    public static <T, R> Iterator<R> map(Iterator<T> iterator, Function<T, R> map) {
        return new MappedIterator<T, R>(iterator, map);
    }

    public static <T, R> Iterator<R> filterAndMap(Iterator<T> iterator, Predicate<T> filter, Function<T, R> map) {
        return map(filter(iterator, filter), map);
    }



    private static class SelectiveIterator<T> extends PolyfillIterator<T> {
        private final Iterator<T> iterator;
        private final Predicate<T> filter;

        private T next;
        private boolean hasNext;

        public SelectiveIterator(Iterator<T> iterator, Predicate<T> filter) {
            this.iterator = iterator;
            this.filter = filter;
        }

        @Override
        public boolean hasNext() {
            while (!hasNext) {
                if (!iterator.hasNext()) return false;
                next = iterator.next();
                hasNext = filter.test(next);
            }
            return true;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            hasNext = false;
            return next;
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }


    private static class MappedIterator<T, R> extends PolyfillIterator<R> {
        private final Iterator<T> iterator;
        private final Function<T, R> map;

        public MappedIterator(Iterator<T> iterator, Function<T, R> map) {
            this.iterator = iterator;
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public R next() {
            return map.apply(iterator.next());
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}