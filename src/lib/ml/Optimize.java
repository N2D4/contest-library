package lib.ml;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optimize {
    long imin() default 0;
    long imax() default 100;
    double fmin() default 0;
    double fmax() default 1;
}
