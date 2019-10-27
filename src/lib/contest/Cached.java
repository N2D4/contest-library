package lib.contest;

import java.lang.annotation.*;

/**
 * Marks the selected non-static variable to be cached on the disk. The object must be Serializable. The variable should
 * be initialized with some place-holder value, eg. null. When the program is run and a cache file is found for this
 * test case, it will be overwritten with that value. Can not be used on static variables.
 *
 * If the content of the variable changes, change this annotation's value; it will then be considered a different
 * object.
 *
 * Best used in optimization contests.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Cached {
    int value() default -1;
}
