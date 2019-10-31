package lib.ml.optimize;

import java.lang.annotation.*;

/**
 * Uses a genetic mutation algorithm to optimize this variable. Works on variables of numeric types or Random. Note that
 * Optimize variables should still have a sensible default, which will be used if the optimizer is disabled.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optimize {
    long imin() default 0;
    long imax() default 100;
    double fmin() default 0;
    double fmax() default 1;
}
