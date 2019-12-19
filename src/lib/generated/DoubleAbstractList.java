/* Generated by Generificator from AbstractList.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.Utils;

import java.util.Arrays;
import java.util.Iterator;


public abstract class DoubleAbstractList implements DoubleList {
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DoubleList)) return false;
        if (other == this) return true;
        if (((DoubleList) other).size() != this.size()) return false;

        DoubleIterator iter1 = this.iterator();
        DoubleIterator iter2 = ((DoubleList) other).iterator();
        while (iter1.hasNext()) {
            if (!Utils.equals(iter1.next(), iter2.next())) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int res = 1;
        for (DoubleIterator iter = this.iterator(); iter.hasNext();) {
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