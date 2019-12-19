/* Generated by Generificator on Thu Dec 19 21:12:34 CET 2019 from List.java */

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


public interface IntList extends IntCollection {
    int[] toArray();
    int size();
    int get(int index);
    int set(int index, int val);
    void add(int val);
    void add(int index, int val);
    void addAll(int[] val);
    void addAll(int index, int[] val);
    int remove(int index);
    void removeAll(Range range);
    void clear();
    IntIterator iterator();
    void reverse();
    default void swap(int i, int j) {
        int tmp = get(i);
        set(i, get(j));
        set(j, tmp);
    }
}