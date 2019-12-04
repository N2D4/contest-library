package lib.utils.various;

import lib.generated.IntExtendedStream;
import lib.generated.IntIntPair;
import lib.generated.IntIterable;
import lib.generated.IntIterator;
import lib.utils.Arr;
import lib.utils.Utils;
import lib.utils.tuples.Pair;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Range extends IntIntPair implements IntIterable {

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
        return this.stream().toArray();
    }

    public LongRange toLongRange() {
        return new LongRange(this.a, this.b);
    }

    public IntExtendedStream stream() {
        return Utils.stream(IntStream.range(a, b));
    }

    @Override
    public IntIterator iterator() {
        return new IntIterator() {
            int c = getLeft();
            @Override
            public boolean hasNext() {
                return c < getRight();
            }

            @Override
            public int next() {
                return c++;
            }
        };
    }

    public boolean contains(int l) {
        return l >= this.a && l < this.b;
    }

    public void intersectWith(Range range) {
        a = Math.max(range.a, a);
        b = Math.min(range.b, b);
    }

    public Range intersectedWith(Range range) {
        Range res = new Range(this);
        res.intersectWith(range);
        return res;
    }

    public static Range getIntersection(Range... a) {
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
