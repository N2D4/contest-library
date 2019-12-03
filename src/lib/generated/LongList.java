/* Generated by Generificator on Tue Dec 03 15:26:20 CET 2019 from List.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.generated.IntList;
import lib.utils.Utils;
import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.DoubleStream;
import java.util.Iterator;
import java.lang.Iterable;


public interface LongList extends LongIterable, Serializable {
    long[] toArray();
    int size();
    long get(int index);
    long set(int index, long val);
    void add(long val);
    void add(int index, long val);
    void addAll(long[] val);
    void addAll(int index, long[] val);
    long remove(int index);
    void removeAll(Range range);
    void clear();
    LongIterator iterator();
    void reverse();
    default void swap(int i, int j) {
        long tmp = get(i);
        set(i, get(j));
        set(j, tmp);
    }

    default LongStream stream() {
        return Utils.stream(iterator());
    }
}