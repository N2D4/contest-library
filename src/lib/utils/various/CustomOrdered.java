package lib.utils.various;

import java.io.Serializable;
import java.util.Objects;

public class CustomOrdered<T> implements Comparable<CustomOrdered<T>>, Serializable {

    private final double order;
    private final T t;

    public CustomOrdered(double order, T t) {
        this.order = order;
        this.t = t;
    }

    public double getOrder() {
        return order;
    }

    public T get() {
        return t;
    }

    @Override
    public int compareTo(CustomOrdered<T> o) {
        return Double.compare(getOrder(), o.getOrder());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other.getClass() != CustomOrdered.class) return false;

        CustomOrdered obj = (CustomOrdered) other;
        if (obj.getOrder() != this.getOrder()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOrder());
    }
}
