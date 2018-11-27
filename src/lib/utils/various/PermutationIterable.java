package lib.utils.various;

import lib.polyfill.PolyfillIterator;
import lib.utils.MathUtils;
import lib.utils.Utils;

import java.util.*;

/**
 * Uses Heap's algorithm
 */
public class PermutationIterable<T> implements Iterable<List<T>> {
    private List<T> list;

    public PermutationIterable(Iterable<T> toPermute) {
        this.list = Utils.toArrayList(toPermute);
    }

    public int size() {
        return MathUtils.factorial(list.size());
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new PolyfillIterator<List<T>>() {
            List<T> cur = new ArrayList<T>(list);
            int[] c = new int[cur.size()];

            {
                c[0] = -1;
            }

            @Override
            public boolean hasNext() {
                for (int i = 0; i < c.length; i++) {
                    if (c[i] < i) return true;
                }
                return false;
            }

            @Override
            public List<T> next() {
                for (int i = 0; i < c.length; i++) {
                    if (c[i] >= i) {
                        c[i] = 0;
                    } else {
                        swap(i % 2 == 0 ? 0 : c[i], i);
                        c[i]++;
                        return cur;
                    }
                }
                throw new NoSuchElementException();
            }

            private void swap(int a, int b) {
                if (a < 0) return;
                T temp = cur.get(a);
                cur.set(a, cur.get(b));
                cur.set(b, temp);
            }
        };
    }
}
