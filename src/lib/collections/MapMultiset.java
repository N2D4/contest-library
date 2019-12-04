package lib.collections;

import lib.utils.Utils;
import lib.utils.tuples.Pair;
import lib.utils.various.ExtendedStream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapMultiset<T> extends AbstractMultiset<T> {
    private final Map<T, Integer> map;
    private int sizeL = 0;

    public MapMultiset(Map<T, Integer> map) {
        this.map = map;
        this.iterator().forEachRemaining(a -> this.sizeL++);
    }

    public MapMultiset(Map<T, Integer> map, Collection<T> c) {
        this(map);
        this.addAll(c);
    }

    @Override
    public Iterator<T> iterator() {
        return Utils.stream(map.entrySet()).flatMap(a -> Collections.nCopies(a.getValue(), a.getKey()).stream()).iterator();
    }

    @Override
    public Set<T> toSet() {
        return map.keySet();
    }

    @Override
    public Set<Pair<T, Integer>> entrySet() {
        return entryStream().collect(Collectors.toSet());
    }

    @Override
    public ExtendedStream<Pair<T, Integer>> entryStream() {
        return Utils.stream(map.entrySet())
                .map(a -> new Pair<>(a.getKey(), a.getValue()))
                .filter(a -> a.b > 0);
    }

    @Override
    public Iterator<Pair<T, Integer>> entryIterator() {
        return entryStream().iterator();
    }

    @Override
    public int size() {
        return this.sizeL;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.count(o) > 0;
    }

    @Override
    public int count(Object o) {
        return this.map.getOrDefault(o, 0);
    }

    @Override
    public boolean add(T t) {
        return this.addN(1, t);
    }

    @Override
    public boolean addN(int n, T t) {
        this.map.put(t, this.count(t) + n);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return removeN(1, o);
    }

    @Override
    public boolean removeAll(Object o) {
        return removeN(this.count(o), o);
    }

    @Override
    public boolean removeN(int n, Object o) {
        int count = this.count(o);
        if (count == 0) return false;
        else if (count <= n) this.map.remove(o);
        else this.map.put((T) o, this.count(o) - n);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c instanceof Multiset) {
            boolean modified = false;
            for (Iterator<Pair<Object, Integer>> it = ((Multiset) c).entryIterator(); it.hasNext();) {
                Pair<Object, Integer> t = it.next();
                if (this.addN(t.b, (T) t.a)) modified = true;
            }
            return modified;
        } else {
            return super.addAll(c);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c instanceof Multiset) {
            boolean modified = false;
            for (Iterator<Pair<Object, Integer>> it = ((Multiset) c).entryIterator(); it.hasNext();) {
                Pair<Object, Integer> t = it.next();
                if (this.removeN(t.b, t.a)) modified = true;
            }
            return modified;
        } else {
            return super.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<Pair<T, Integer>> it = entryIterator();
        while (it.hasNext()) {
            if (!c.contains(it.next().a)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

}
