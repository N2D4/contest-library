package lib.algorithms;

import java.lang.annotation.*;

/**
 * An annotation used to add big-o notation information to a function/algorithm. This information will never be
 * available at runtime, and usages of this annotation may or may not be removed by the code bundler.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface O {
    String value();
}
