/* Generated by Generificator from Iterator.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.function.Consumer;


public interface DoubleIterator {
    double next();
    boolean hasNext();
    default void remove() {
        throw new UnsupportedOperationException();
    }

    default void forEachRemaining(DoubleConsumer consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }
}
