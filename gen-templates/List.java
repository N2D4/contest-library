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

/* GENERIFY-THIS */
public interface List<T> extends Iterable<T>, Serializable {
    /*BOX T*/Object[] toArray();
    int size();
    T get(int index);
    T set(int index, T val);
    void add(T val);
    void add(int index, T val);
    void addAll(T[] val);
    void addAll(int index, T[] val);
    T remove(int index);
    void removeAll(Range range);
    void clear();
    Iterator<T> iterator();
    void reverse();
    default void swap(int i, int j) {
        T tmp = get(i);
        set(i, get(j));
        set(j, tmp);
    }

    default Stream<T> stream() {
        return Utils.stream(iterator());
    }
}