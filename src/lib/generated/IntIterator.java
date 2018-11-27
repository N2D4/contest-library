package lib.generated;

import java.util.function.IntConsumer;

public interface IntIterator {
    boolean hasNext();
    int next();


    default void remove() {
        throw new UnsupportedOperationException();
    }
    default void forAllRemaining(IntConsumer consumer) {
        while (this.hasNext()) {
            consumer.accept(next());
        }
    }
}
