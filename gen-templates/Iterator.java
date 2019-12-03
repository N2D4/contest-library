import java.util.function.Consumer;

/* GENERIFY-THIS */
public interface Iterator<T> {
    T next();
    boolean hasNext();
    default void remove() {
        throw new UnsupportedOperationException();
    }

    default void forEachRemaining(Consumer<T> consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }
}
