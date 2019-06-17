package lib.generated;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface IntIterator extends PrimitiveIterator.OfInt {
    /**
     * Returns a view of this iterator which does not log error messages when using next() instead of nextInt().
     */
    default Iterator<Integer> untripped() {
        IntIterator it = this;
        return new Iterator<Integer>() {
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
            public void forEachRemaining(Consumer<? super Integer> action) {
                it.forEachRemaining(action);
            }
        };
    }
}
