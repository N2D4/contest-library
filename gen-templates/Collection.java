import lib.utils.Utils;
import lib.utils.various.ExtendedStream;
import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Iterator;
import java.util.stream.Stream;

/* GENERIFY-THIS */
public interface Collection<T> extends Iterable<T>, Serializable {
    int size();
    void add(T val);
    T remove(int index);
    Iterator<T> iterator();

    default ExtendedStream<T> stream() {
        return Utils.stream(iterator());
    }
}
