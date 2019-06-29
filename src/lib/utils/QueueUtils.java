package lib.utils;

import java.util.*;
import java.util.function.Function;

public final class QueueUtils {

    public static <T> Queue<T> createFIFO() {
        return new ArrayDeque<>();
    }

    public static <T> Queue<T> createLIFO() {
        return Collections.asLifoQueue(new ArrayDeque<>());
    }

    public static <T extends Comparable<? super T>> Queue<T> createPriority() {
        return new PriorityQueue<>();
    }

    public static <T> Queue<T> createPriority(Comparator<? super T> comparator) {
        return new PriorityQueue<>(comparator);
    }

    public static <T, U extends Comparable<? super U>> Queue<T> createPriority(Function<? super T, ? extends U> keyExtractor) {
        return new PriorityQueue<>(Comparator.comparing(keyExtractor));
    }
}
