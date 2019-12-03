package lib.utils.various;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class FlagSet extends AbstractSet<Integer> implements Set<Integer>, Serializable {

    private boolean[] arr;
    int size = -1;

    public FlagSet(boolean[] arr) {
        this.arr = arr;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int cur = 0;

            @Override
            public boolean hasNext() {
                while (cur < arr.length) {
                    if (arr[cur]) return true;
                    cur++;
                }
                return false;
            }

            @Override
            public Integer next() {
                return cur;
            }
        };
    }

    @Override
    public int size() {
        if (size == -1)
            this.iterator().forEachRemaining(a -> size++);
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Integer && o != null && arr[(int) o];
    }
}
