package lib.utils.function;

/* GENERIFY-THIS */
@FunctionalInterface
public interface Pred<T> {
    boolean test(T t);
}
