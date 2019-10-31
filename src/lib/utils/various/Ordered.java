package lib.utils.various;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A wrapper class allowing you to provide a custom comparison object. Two objects are considered equal if the
 * custom comparison object is.
 */
public class Ordered<T, C extends Comparable<? super C>> implements Comparable<Ordered<T, C>>, Serializable {

    private final T t;
    private final C order;

    public Ordered(T t, C order) {
        this.order = order;
        this.t = t;
    }

    public C getOrder() {
        return order;
    }

    public T get() {
        return t;
    }

    @Override
    public int compareTo(Ordered<T, C> o) {
        return this.getOrder().compareTo(o.getOrder());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Ordered)) return false;

        Ordered obj = (Ordered) other;
        return Objects.equals(this.getOrder(), obj.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOrder());
    }

    @Override
    public String toString() {
        return "(" + t + ", " + order + ")";
    }
}
