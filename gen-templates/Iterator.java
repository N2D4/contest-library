package lib.generated;

import java.util.function.IntConsumer;

public interface $CAP_TYPE$Iterator extends PrimitiveIterator<$WRAPPER_TYPE$, $THROW_ERROR_UNIMPL$> {
    /**
     * Returns a view of this iterator which does not log error messages when using next() instead of nextInt().
     */
    default Iterator<$WRAPPER_TYPE$> untripped() {
        IntIterator it = this;
        return new Iterator<$WRAPPER_TYPE$>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Integer next() {
                return it.nextInt();
            }

            @Override
            public void remove() {
                it.remove();
            }

            @Override
            public void forEachRemaining(Consumer<? super $WRAPPER_TYPE$> action) {
                it.forEachRemaining(action);
            }
        };
    }
}
