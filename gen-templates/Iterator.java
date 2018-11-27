package lib.generated;

import java.util.function.IntConsumer;

public interface $CAP_TYPE$Iterator {
    boolean hasNext();
    $TYPE$ next();


    default void remove() {
        throw new UnsupportedOperationException();
    }
    default void forAllRemaining($UPGRADED_TYPE$Consumer consumer) {
        while (this.hasNext()) {
            consumer.accept(next());
        }
    }
}
