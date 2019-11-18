import java.util.function.Consumer;

/* GENERIFY-THIS */
public interface Iterator<T> {
    T next();
    boolean hasNext();
    default void forEachRemaining(Consumer<T> consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }
}
