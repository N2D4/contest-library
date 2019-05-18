package lib.collections;

import java.util.AbstractCollection;

public abstract class AbstractMultiSet<T> extends AbstractCollection<T> implements MultiSet<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof MultiSet))
            return false;
        if (obj == this)
            return true;

        return this.entrySet().equals(((MultiSet) obj).entrySet());
    }

    @Override
    public int hashCode() {
        return this.entrySet().hashCode();
    }


}
