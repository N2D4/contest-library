package lib.algorithms;

import lib.utils.ArrayUtils;
import lib.utils.tuples.Pair;
import lib.utils.various.Range;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Histograms extends Algorithm {
    private Histograms() {
        // Quite dusty here...
    }


    /**
     * Computes the maximum histogram area between the 0-line and the heights given. Each array element is assumed to
     * have a width of 1.
     */
    public static Pair<Range, Integer> maxArea(int[] arr) {
        return maxArea(ArrayUtils.asLongArray(arr)).map(a -> a, b -> (int) (long) b);
    }

    /**
     * Computes the maximum histogram area between the 0-line and the heights given. Each array element is assumed to
     * have a width of 1.
     */
    public static Pair<Range, Long> maxArea(long[] arr) {
        Deque<Integer> s = new ArrayDeque<>();

        long max = 0;
        Range maxRange = new Range(0, 0);
        int i;
        for (i = 0; i < arr.length || !s.isEmpty();) {
            if (s.isEmpty() || (i < arr.length && arr[i] >= arr[s.peek()])) {
                s.push(i++);
            } else {
                int top = s.pop();
                int from = s.isEmpty() ? 0 : s.peek() + 1;
                long cur = arr[top] * (i - from);
                if (cur > max) {
                    max = cur;
                    maxRange = new Range(from, i);
                }
            }
        }

        return new Pair<>(maxRange, max);
    }

}
