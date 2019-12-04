package lib.utils.various;

import lib.generated.LongExtendedStream;
import lib.generated.LongIterable;
import lib.generated.LongIterator;
import lib.generated.LongLongPair;
import lib.utils.Arr;
import lib.utils.Utils;
import lib.utils.tuples.Pair;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.LongStream;

public class LongRange extends LongLongPair implements LongIterable {

    public LongRange(long i, long j) {
        super(i, j);
    }
    public LongRange(LongRange range) {
        this(range.a, range.b);
    }

    public long size() {
        return this.b - this.a;
    }

    public long[] toLongArray() {
        return this.stream().toArray();
    }

    public Range toIntRange() {
        return new Range((int) (long) this.a, (int) (long) this.b);
    }

    public LongExtendedStream stream() {
        return Utils.stream(LongStream.range(a, b));
    }

    @Override
    public LongIterator iterator() {
        return new LongIterator() {
            long c = getLeft();
            @Override
            public boolean hasNext() {
                return c < getRight();
            }

            @Override
            public long next() {
                return c++;
            }
        };
    }

    public boolean contains(long l) {
        return l >= this.a && l < this.b;
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
        return Arr.stream(a).reduce((res, b) -> res.intersectedWith(b)).get();
    }

    public void ensureValidity() throws IllegalArgumentException {
        if (this.b < this.a) throw new IllegalArgumentException("Second argument of range (" + this.a + ", " + this.b + ") must be larger than first");
    }

    @Override
    public String toString() {
        return "[" + a + ", " + b + ")";
    }
}
