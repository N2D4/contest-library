/* Generated by Generificator on Thu Dec 19 21:12:34 CET 2019 from Collection.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.Utils;
import lib.utils.various.ExtendedStream;
import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Iterator;
import java.util.stream.Stream;


public interface LongCollection extends LongIterable, Serializable {
    int size();
    void add(long val);
    long remove(int index);
    LongIterator iterator();

    default LongExtendedStream stream() {
        return Utils.stream(iterator());
    }
}
