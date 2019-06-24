package lib.collections;

import java.util.AbstractCollection;

public abstract class AbstractMultiset<T> extends AbstractCollection<T> implements Multiset<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Multiset))
            return false;
        if (obj == this)
            return true;

        return this.entrySet().equals(((Multiset) obj).entrySet());
    }

    @Override
    public int hashCode() {
        return this.entrySet().hashCode();
    }


}
