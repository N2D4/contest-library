package lib.utils.function;

/* GENERIFY-THIS */
@FunctionalInterface
public interface BiCons<T, U> {
    void accept(T t, U u);
}
