package lib.utils.various;

@FunctionalInterface
public interface ThrowingPredicate<T, E extends Throwable> {
    boolean test(T t) throws E;
}
