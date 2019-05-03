package lib.algorithms;

import lib.utils.ArrayUtils;
import lib.utils.tuples.Pair;
import lib.utils.various.Range;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public final class BinarySearch extends Algorithm {
    private BinarySearch() {
        // Quite dusty here...
    }


    public static Range search(int[] array, Range searchForRange) {
        return search(ArrayUtils.asList(array), searchForRange);
    }

    public static Range search(int[] array, int searchFor) {
        return search(ArrayUtils.asList(array), searchFor);
    }





    /**
     * Returns the first i for which isBigger(i) is true, assuming it's false for all j < i and true for all j >= i
     */
    public static <T> long searchInRange(Range range, Predicate<Long> isBigger) {
        long a = range.a;
        long b = range.b;
        while (a < b) {
            long mid = (a + b) / 2;
            if (isBigger.test(mid)) {
                b = mid;
            } else {
                a = mid + 1;
            }
        }
        return a;
    }

    private static <T> int search(List<T> list, Predicate<T> isBigger) {
        return (int) searchInRange(new Range(0, list.size()), (i) -> isBigger.test(list.get((int) (long) i)));
    }

    private static <T> Range search(List<T> list, Predicate<T> isBiggerA, Predicate<T> isBiggerB) {
        return new Range(search(list, isBiggerA), search(list, isBiggerB));
    }


    public static <T> Range search(List<T> list, Comparator<T> comparator, T searchFor) {
        return search(list, a -> comparator.compare(a, searchFor) >= 0, b -> comparator.compare(b, searchFor) > 0);
    }

    public static <T extends Comparable<? super T>> Range search(List<T> list, T searchFor) {
        return search(list, Comparator.naturalOrder(), searchFor);
    }


    public static <T> Range search(List<T> list, Comparator<T> comparator, Pair<T, T> searchForRange) {
        return search(list, a -> comparator.compare(a, searchForRange.a) >= 0, b -> comparator.compare(b, searchForRange.b) >= 0);
    }

    public static <T extends Comparable<? super T>> Range search(List<T> list, Pair<T, T> searchForRange) {
        return search(list, Comparator.naturalOrder(), searchForRange);
    }


}
