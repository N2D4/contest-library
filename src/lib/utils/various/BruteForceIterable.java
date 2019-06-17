package lib.utils.various;

import lib.polyfill.PolyfillIterator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BruteForceIterable implements Iterable<int[]> {
    private int[] max;
    private int[] min;

    public BruteForceIterable(int[] min, int[] max) {
        if (min == null) {
            min = new int[max.length];
        }
        if (min.length != max.length) throw new RuntimeException("min.length != max.length");
        this.min = min;
        this.max = max;
    }

    public BruteForceIterable(int[] max) {
        this(null, max);
    }

    public int size() {
        int result = 1;
        for (int i = 0; i < max.length; i++) {
            result *= (max[i] - min[i]);
        }
        return result;
    }

    @Override
    public Iterator<int[]> iterator() {
        for (int i = 0; i < min.length; i++) {
            if (min[i] >= max[i]) return Collections.emptyIterator();
        }
        return new PolyfillIterator<int[]>() {
            int[] cur = Arrays.copyOf(min, min.length);

            {
                cur[cur.length - 1]--;
            }

            @Override
            public boolean hasNext() {
                for (int i = 0; i < cur.length; i++) {
                    if (cur[i] < max[i] - 1) return true;
                }
                return false;
            }

            @Override
            public int[] next() {
                for (int i = cur.length - 1; i >= 0; i--) {
                    cur[i]++;
                    if (cur[i] >= max[i]) {
                        cur[i] = min[i];
                    } else {
                        return cur;
                    }
                }
                throw new NoSuchElementException();
            }
        };
    }
}
