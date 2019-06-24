package lib.collections;

import java.util.Collection;
import java.util.TreeMap;

public class TreeMultiset<T> extends MapMultiset<T> {
    public TreeMultiset() {
        super(new TreeMap<>());
    }

    public TreeMultiset(Collection<T> c) {
        super(new TreeMap<>(), c);
    }

}
