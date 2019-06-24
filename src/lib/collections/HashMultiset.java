package lib.collections;

import java.util.Collection;
import java.util.HashMap;

public class HashMultiset<T> extends MapMultiset<T> {
    public HashMultiset() {
        super(new HashMap<>());
    }

    public HashMultiset(Collection<T> c) {
        super(new HashMap<>(), c);
    }

    public HashMultiset(int initialCapacity) {
        super(new HashMap<>(initialCapacity));
    }

}
