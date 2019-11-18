import lib.utils.Utils;

import java.util.Arrays;
import java.util.Iterator;

/* GENERIFY-THIS */
public abstract class AbstractList<T> implements List<T> {
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof /*PREFIX T*/List)) return false;
        if (other == this) return true;
        if (((List<T>) other).size() != this.size()) return false;

        Iterator<T> iter1 = this.iterator();
        Iterator<T> iter2 = ((List<T>) other).iterator();
        while (iter1.hasNext()) {
            if (!Utils.equals(iter1.next(), iter2.next())) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int res = 1;
        for (Iterator<T> iter = this.iterator(); iter.hasNext();) {
            res *= 31;
            res += Utils.hashCode(iter.next());
        }
        return res;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.toArray());
    }
}