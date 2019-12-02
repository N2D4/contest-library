/* Generated by Generificator on Mon Dec 02 16:10:31 CET 2019 from AbstractList.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.Utils;

import java.util.Arrays;
import java.util.Iterator;


public abstract class IntAbstractList implements IntList {
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IntList)) return false;
        if (other == this) return true;
        if (((IntList) other).size() != this.size()) return false;

        IntIterator iter1 = this.iterator();
        IntIterator iter2 = ((IntList) other).iterator();
        while (iter1.hasNext()) {
            if (!Utils.equals(iter1.next(), iter2.next())) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int res = 1;
        for (IntIterator iter = this.iterator(); iter.hasNext();) {
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