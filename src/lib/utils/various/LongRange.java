package lib.utils.various;

import lib.polyfill.PolyfillIterator;
import lib.utils.tuples.Pair;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.LongStream;

public class LongRange extends Pair<Long, Long> implements Iterable<Long> {

    public LongRange(long i, long j) {
        super(i, j);
    }
    public LongRange(LongRange range) {
        this(range.a, range.b);
    }

    public long size() {
        return this.b - this.a;
    }

    public long[] toIntArray() {
        return this.stream().toArray();
    }


    /* BEGIN-JAVA-8 */
    public LongStream stream() {
        return LongStream.range(a, b);
    }
    /* END-JAVA-8 */

    @Override
    public Iterator<Long> iterator() {
        return new PolyfillIterator<Long>() {
            long c = getLeft();
            @Override
            public boolean hasNext() {
                return c < getRight();
            }

            @Override
            public Long next() {
                return c++;
            }
        };
    }

    public void intersectWith(LongRange range) {
        a = Math.max(range.a, a);
        b = Math.min(range.b, b);
    }

    public LongRange intersectedWith(LongRange range) {
        LongRange res = new LongRange(this);
        res.intersectWith(range);
        return res;
    }

    public static LongRange getIntersection(LongRange... a) {
        return Arrays.stream(a).reduce((res, b) -> res.intersectedWith(b)).get();
    }

    public void ensureValidity() throws IllegalArgumentException {
        if (this.b < this.a) throw new IllegalArgumentException("Second argument of range (" + this.a + ", " + this.b + ") must be larger than first");
    }
}
