package lib.collections;

import lib.utils.tuples.Pair;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public interface Multiset<E> extends Collection<E> {
    int count(Object o);
    boolean addN(int n, E e);
    boolean removeN(int n, Object o);
    boolean removeAll(Object o);
    Set<E> toSet();
    Set<Pair<E, Integer>> entrySet();
    Stream<Pair<E, Integer>> entryStream();
    Iterator<Pair<E, Integer>> entryIterator();
}
