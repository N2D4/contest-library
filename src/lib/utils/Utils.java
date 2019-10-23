package lib.utils;

/* BEGIN-JAVA-8 */
import java.util.*;
import java.util.function.Consumer;
/* END-JAVA-8 */

import java.util.function.Supplier;
import java.util.stream.*;

public final class Utils {
    private Utils() {
        // Quite dusty here...
    }


    /* BEGIN-JAVA-8 */
    public static void repeat(int times, Consumer<Integer> consumer) {
        for (int i = 0; i < times; i++) {
            consumer.accept(i);
        }
    }

    public static void repeat(int times, Runnable runnable) {
        repeat(times, (a) -> runnable.run());
    }


    public static double timing(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000_000.0;
    }

    private static int timingId = 0;
    public static <T> T printTiming(Supplier<T> supplier, String name) {
        int tid = timingId++;
        if (name == null) name = "" + tid;
        else name = tid + " - " + name;
        System.err.println("Starting task " + name);
        long start = System.nanoTime();
        try {
            return supplier.get();
        } finally {
            long end = System.nanoTime();
            System.err.println("Time taken for task " + name + ": " + (end - start) / 1_000_000_000.0 + "s");
        }
    }

    public static void printTiming(Runnable runnable, String name) {
        printTiming(() -> {
            runnable.run();
            return null;
        }, name);
    }

    public static <T> T printTiming(Supplier<T> supplier) {
        return printTiming(supplier, null);
    }

    public static void printTiming(Runnable runnable) {
        printTiming(runnable, null);
    }
    /* END-JAVA-8 */




    public static <T> ArrayList<T> toArrayList(Iterable<T> iterable) {
        int size = iterable instanceof Collection ? ((Collection) iterable).size() : -1;
        return toArrayList(iterable, size);
    }

    public static <T> ArrayList<T> toArrayList(Iterable<T> iterable, int size) {
        return toArrayList(iterable.iterator(), size);
    }


    public static <T> ArrayList<T> toArrayList(Iterator<T> iterator) {
        return toArrayList(iterator, -1);
    }

    public static <T> ArrayList<T> toArrayList(Iterator<T> iterator, int estimatedSize) {
        return toArrayList(Spliterators.spliterator(iterator, estimatedSize, Spliterator.ORDERED));
    }

    public static <T> ArrayList<T> toArrayList(Spliterator<T> spliterator) {
        long estimatedSize = spliterator.estimateSize();
        if (estimatedSize >= Long.MAX_VALUE || estimatedSize <= 10) estimatedSize = 10;
        ArrayList<T> result = new ArrayList<>((int) Math.min(estimatedSize, Integer.MAX_VALUE));
        spliterator.forEachRemaining(result::add);
        return result;
    }


    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return stream(iterator, -1);
    }

    public static <T> Stream<T> stream(Iterator<T> iterator, int estimatedSize) {
        return stream(Spliterators.spliterator(iterator, estimatedSize, Spliterator.ORDERED));
    }

    public static <T> Stream<T> stream(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }


    /**
     * Returns a map containing each element of the collection and a set of its indices in the iterator order as a
     * key-value pair. The sets are immutable and ordered.
     *
     * Usually used in lists. Often used on arrays in conjunction with ArrayUtils.asList(...).
     */
    public static <T> Map<T, Set<Integer>> invert(Collection<T> coll) {
        Map<T, Set<Integer>> map = new HashMap<>();
        int i = 0;
        for (T t : coll) {
            Set<Integer> set = map.get(t);
            if (set == null) {
                map.put(t, Collections.singleton(i));
            } else {
                if (!(set instanceof LinkedHashSet)) {
                    set = new LinkedHashSet<>(set);
                    map.put(t, set);
                }
                set.add(i);
            }
            i++;
        }
        return Collections.unmodifiableMap(map);
    }




    public static <T> ArrayList<T> arrayListOfSize(int size) {
        return arrayListOfSize(size, null);
    }

    public static <T> ArrayList<T> arrayListOfSize(int size, T element) {
        ArrayList<T> result = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            result.add(element);
        }
        return result;
    }


    public static <T> int hashAll(Iterator<T> iterator) {
        int hash = 1;
        while (iterator.hasNext()) {
            T obj = iterator.next();
            hash = hash * 31 + Objects.hashCode(obj);
        }
        return hash;
    }


    /**
     * Returns a Set given a collection of **unique** elements. If the collection contains duplicates, the Set will too
     * (which is, per the definition of a Set, not allowed).
     *
     * The returned set is unmodifiable.
     */
    public static <T> Set<T> asSet(Collection<T> uniqueElements) {
        return asModifiableSet(Collections.unmodifiableCollection(uniqueElements));
    }

    /**
     * Returns a Set given a collection of **unique** elements. If the collection contains duplicates, the Set will too
     * (which is, per the definition of a Set, not allowed). The underlying collection must make sure newly added
     * elements are not duplicates.
     *
     * The returned set is modifiable if and only if the underlying collection is modifiable.
     */
    public static <T> Set<T> asModifiableSet(Collection<T> uniqueElements) {
        return new AbstractSet<T>() {
            @Override
            public int size() {
                return uniqueElements.size();
            }

            @Override
            public boolean isEmpty() {
                return uniqueElements.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return uniqueElements.contains(o);
            }

            @Override
            public Iterator<T> iterator() {
                return uniqueElements.iterator();
            }

            @Override
            public Object[] toArray() {
                return uniqueElements.toArray();
            }

            @Override
            public <T1> T1[] toArray(T1[] a) {
                return uniqueElements.toArray(a);
            }

            @Override
            public boolean add(T t) {
                return uniqueElements.add(t);
            }

            @Override
            public boolean remove(Object o) {
                return uniqueElements.remove(o);
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return uniqueElements.containsAll(c);
            }

            @Override
            public boolean addAll(Collection<? extends T> c) {
                return uniqueElements.addAll(c);
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return uniqueElements.retainAll(c);
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return uniqueElements.removeAll(c);
            }

            @Override
            public void clear() {
                uniqueElements.clear();
            }
        };
    }

    /**
     * Returns a Collector which collects **unique** elements to a set. If the elements passed to it contains
     * duplicates, the Set will too (which is, per the definition of a Set, not allowed).
     *
     * This is functionally equivalent to
     * `Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet)`, but likely more performant.
     */
    public static <T> Collector<T, ?, Set<T>> collectToSet() {
        return Collectors.collectingAndThen(Collectors.toList(), Utils::asSet);
    }

    /**
     * Returns a reverse list iterator given a list. The iterator will start at the last element and end at the first.
     */
    public static <T> ListIterator<T> reverseIterator(List<T> list) {
        return reverseIterator(list.listIterator(list.size()));
    }

    /**
     * Returns a new iterator being the reverted list iterator. Modifying this iterator will modify the original
     * iterator.
     */
    public static <T> ListIterator<T> reverseIterator(ListIterator<T> it) {
        return new ListIterator<T>() {
            @Override
            public boolean hasNext() {
                return it.hasPrevious();
            }

            @Override
            public T next() {
                return it.previous();
            }

            @Override
            public boolean hasPrevious() {
                return it.hasNext();
            }

            @Override
            public T previous() {
                return it.next();
            }

            @Override
            public int nextIndex() {
                return it.previousIndex();
            }

            @Override
            public int previousIndex() {
                return it.nextIndex();
            }

            @Override
            public void remove() {
                it.remove();
            }

            @Override
            public void set(T t) {
                it.set(t);
            }

            @Override
            public void add(T t) {
                it.add(t);
            }
        };
    }



    /**
     * Returns an iterable with a reverse iterator of this list, backed by the original list.
     */
    public static <T> Iterable<T> reverseIterable(List<T> list) {
        return () -> reverseIterator(list);
    }




    private static final Map<Class, Class> primitiveClassWrappers = new HashMap<Class, Class>() {{
        put(boolean.class, Boolean.class);
        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(char.class, Character.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
    }};
    public static Class<?> primitiveToWrapper(Class<?> clss) {
        return primitiveClassWrappers.getOrDefault(clss, clss);
    }

}