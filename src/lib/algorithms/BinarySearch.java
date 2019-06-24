package lib.algorithms;

import lib.utils.ArrayUtils;
import lib.utils.tuples.Pair;
import lib.utils.various.LongRange;
import lib.utils.various.Range;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
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
     * Returns the first i for which isBigger(i) is true (usually it's false for all j < i and true for all j >= i)
     */
    public static int searchInRange(Range range, IntPredicate isBigger) {
        return (int) searchInRange(range.toLongRange(), l -> isBigger.test((int) (long) l));
    }

    /**
     * Returns the first i for which isBigger(i) is true (usually it's false for all j < i and true for all j >= i).
     */
    public static long searchInRange(LongRange range, LongPredicate isBigger) {
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

    /**
     * Returns the first i for which isBigger(i) is true (usually it's false for all j < i and true for all j >= i).
     *
     * Range is [range.a, range.b) (so inclusive begin, exclusive end).
     */
    public static BigInteger searchInRange(Pair<BigInteger, BigInteger> range, Predicate<BigInteger> isBigger) {
        BigInteger a = range.a;
        BigInteger b = range.b;
        while (a.compareTo(b) < 0) {
            BigInteger mid = a.add(b).divide(BigInteger.valueOf(2));
            if (isBigger.test(mid)) {
                b = mid;
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a;
    }

    private static <T> int search(List<T> list, Predicate<T> isBigger) {
        return searchInRange(new Range(0, list.size()), (i) -> isBigger.test(list.get((int) (long) i)));
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
