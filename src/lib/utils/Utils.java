package lib.utils;

/* BEGIN-JAVA-8 */
import java.util.*;
import java.util.function.Consumer;
/* END-JAVA-8 */

import java.util.function.Supplier;

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
        return (start - end) / 1_000_000_000.0;
    }

    private static int timingId = 0;
    public static <T> T printTiming(Supplier<T> supplier) {
        int tid = timingId++;
        System.out.println("Starting task " + tid);
        long start = System.nanoTime();
        try {
            return supplier.get();
        } finally {
            long end = System.nanoTime();
            System.out.println("Time taken for task " + tid + ": " + (end - start) / 1_000_000_000.0 + "s");
        }
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

    /* BEGIN-JAVA-8 */
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
    /* END-JAVA-8 */

    /* BEGIN-POLYFILL-6 *../
    public static <T> ArrayList<T> toArrayList(Iterator<T> iterator, int estimatedSize) {
        if (estimatedSize == Long.MAX_VALUE || estimatedSize <= 10) estimatedSize = 10;
        ArrayList<T> result = new ArrayList<T>(estimatedSize);
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }
    /..* END-POLYFILL-6 */




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