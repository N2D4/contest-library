package lib.utils.various;

import lib.polyfill.PolyfillIterator;
import lib.utils.tuples.Pair;

import java.util.Iterator;
import java.util.stream.IntStream;

public class Range extends Pair<Integer, Integer> implements Iterable<Integer> {

    public Range(int i, int j) {
        super(i, j);
    }
    public Range(Range range) {
        this(range.a, range.b);
    }

    public int size() {
        return this.b - this.a;
    }

    public int[] toIntArray() {
        return IntStream.range(a, b).toArray();
    }


    /* BEGIN-JAVA-8 */
    public IntStream stream() {
        return IntStream.range(a, b);
    }
    /* END-JAVA-8 */

    @Override
    public Iterator<Integer> iterator() {
        return new PolyfillIterator<Integer>() {
            int c = getLeft();
            @Override
            public boolean hasNext() {
                return c < getRight();
            }

            @Override
            public Integer next() {
                return c++;
            }
        };
    }

    public void intersectWith(Range range) {
        a = Math.max(range.a, a);
        b = Math.min(range.b, b);
    }

    public static Range getIntersection(Range... a) {
        Range result = new Range(Integer.MIN_VALUE, Integer.MAX_VALUE);
        for (Range b : a) {
            result.intersectWith(b);
        }
        return result;
    }

    public void ensureValidity() throws IllegalArgumentException {
        if (this.b < this.a) throw new IllegalArgumentException("Second argument of range (" + this.a + ", " + this.b + ") must be larger than first");
    }
}
