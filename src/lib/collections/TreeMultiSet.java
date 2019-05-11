package lib.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class TreeMultiSet<T> extends MapMultiSet<T> {
    public TreeMultiSet() {
        super(new TreeMap<>());
    }

    public TreeMultiSet(Collection<T> c) {
        super(new TreeMap<>(), c);
    }

}
