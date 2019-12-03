package lib.utils.function;

/* GENERIFY-THIS */
@FunctionalInterface
public interface BiFunc<T, U, R> {
    R apply(T t, U u);
}
