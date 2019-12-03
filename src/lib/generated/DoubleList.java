/* Generated by Generificator on Tue Dec 03 22:32:57 CET 2019 from List.java */

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


public interface DoubleList extends DoubleCollection {
    double[] toArray();
    int size();
    double get(int index);
    double set(int index, double val);
    void add(double val);
    void add(int index, double val);
    void addAll(double[] val);
    void addAll(int index, double[] val);
    double remove(int index);
    void removeAll(Range range);
    void clear();
    DoubleIterator iterator();
    void reverse();
    default void swap(int i, int j) {
        double tmp = get(i);
        set(i, get(j));
        set(j, tmp);
    }
}