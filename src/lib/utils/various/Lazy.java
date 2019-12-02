package lib.utils.various;

import java.util.Optional;
import java.util.function.Supplier;

/* GENERIFY-THIS */
@FunctionalInterface
public interface Lazy<T> extends Supplier<T> {}
