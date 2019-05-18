package lib.collections;

import java.util.Collection;
import java.util.HashMap;

public class HashMultiSet<T> extends MapMultiSet<T> {
    public HashMultiSet() {
        super(new HashMap<>());
    }

    public HashMultiSet(Collection<T> c) {
        super(new HashMap<>(), c);
    }

    public HashMultiSet(int initialCapacity) {
        super(new HashMap<>(initialCapacity));
    }

}
