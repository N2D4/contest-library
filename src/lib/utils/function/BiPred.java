package lib.utils.function;

/* GENERIFY-THIS */
@FunctionalInterface
public interface BiPred<T, U> {
    boolean test(T t, U u);
}
