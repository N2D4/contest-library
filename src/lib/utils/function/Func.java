package lib.utils.function;

/* GENERIFY-THIS */
@FunctionalInterface
public interface Func<T, R> {
    R apply(T t);
}
