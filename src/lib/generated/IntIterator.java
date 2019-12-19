/* Generated by Generificator on Thu Dec 19 21:12:34 CET 2019 from Iterator.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.function.Consumer;


public interface IntIterator {
    int next();
    boolean hasNext();
    default void remove() {
        throw new UnsupportedOperationException();
    }

    default void forEachRemaining(IntConsumer consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }
}
