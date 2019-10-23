package lib.utils;

import java.util.*;
import java.util.function.Function;

public final class QueueUtils {

    /**
     * Creates a FIFO queue (aka. "queue")
     */
    public static <T> Queue<T> createFIFO() {
        return new ArrayDeque<>();
    }

    /**
     * Creates a LIFO queue (aka. "stack"). Alias for createStack()
     */
    public static <T> Queue<T> createLIFO() {
        return Collections.asLifoQueue(new ArrayDeque<>());
    }

    /**
     * Creates a LIFO queue (aka. "stack"). Alias for createLifo()
     */
    public static <T> Queue<T> createStack() {
        return createLIFO();
    }

    public static <T extends Comparable<? super T>> Queue<T> createPriority() {
        return new PriorityQueue<>();
    }

    public static <T> Queue<T> createPriority(Comparator<? super T> comparator) {
        return new PriorityQueue<>(comparator);
    }

    public static <T, U extends Comparable<? super U>> Queue<T> createPriority(Function<? super T, ? extends U> keyExtractor) {
        return createPriority(Comparator.comparing(keyExtractor));
    }
}
