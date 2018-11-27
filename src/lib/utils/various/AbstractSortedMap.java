package lib.utils.various;

import lib.utils.tuples.Pair;

import java.util.*;

public abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

    private final Comparator<? super K> comparator;

    public AbstractSortedMap() {
        this.comparator = null;
    }

    public AbstractSortedMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }


    @Override
    public abstract V get(Object key);

    @Override
    public abstract boolean containsKey(Object key);

    @Override
    public abstract V put(K key, V value);

    @Override
    public abstract V remove(Object key);

    @Override
    public abstract int size();

    public abstract Iterator<Map.Entry<K, V>> entryIterator();




    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }

    @Override
    public K firstKey() {
        return entrySet().first().getKey();
    }

    @Override
    public K lastKey() {
        return entrySet().last().getKey();
    }

    @Override
    public SortedSet<Entry<K, V>> entrySet() {
        return new SortedMapSet<>(this, entryIterator());
    }



    protected AbstractSortedMap<K, V> subMap(K fromKey, boolean recFrom, K toKey, boolean recTo) {
        AbstractSortedMap<K, V> outer = this;
        return new AbstractSortedMap<K, V>() {

            private boolean isInRange(Object obj) {
                Comparator<? super K> cmp = comparator();
                if (cmp == null) {
                    return ((Comparable<? super K>) obj).compareTo(fromKey) >= 0 &&
                            ((Comparable<? super K>) obj).compareTo(toKey) < 0;
                } else {
                    return recFrom && Objects.compare(fromKey, (K) obj, cmp) <= 0 &&
                            recTo && Objects.compare(toKey, (K) obj, cmp) > 0;
                }
            }

            @Override
            public V get(Object key) {
                if (!isInRange(key)) return null;
                return outer.get(key);
            }

            @Override
            public boolean containsKey(Object key) {
                if (!isInRange(key)) return false;
                return outer.containsKey(key);
            }

            @Override
            public V put(K key, V value) {
                if (!isInRange(key)) throw new IllegalArgumentException();
                return outer.put(key, value);
            }

            @Override
            public V remove(Object key) {
                if (!isInRange(key)) return null;
                return outer.remove(key);
            }

            @Override
            public int size() {
                return entrySet().size();
            }

            @Override
            public Iterator<Entry<K, V>> entryIterator() {
                return entrySet().iterator();
            }

            @Override
            public SortedSet<Entry<K, V>> entrySet() {
                return outer.entrySet().subSet(new Pair<K, V>(fromKey, null), new Pair<K, V>(toKey, null));
            }

            @Override
            public K firstKey() {
                return entrySet().first().getKey();
            }

            @Override
            public K lastKey() {
                return entrySet().last().getKey();
            }
        };
    }


    @Override
    public AbstractSortedMap subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, true);
    }

    @Override
    public AbstractSortedMap headMap(K toKey) {
        return subMap(null, false, toKey, true);
    }

    @Override
    public AbstractSortedMap tailMap(K fromKey) {
        return subMap(fromKey, true, null, false);
    }







    private static class SortedMapSet<K, V> extends AbstractSortedSet<Entry<K, V>> {

        private final SortedMap<K, V> asm;
        private final Iterator<Entry<K, V>> entryIterator;

        public SortedMapSet(SortedMap<K, V> asm, Iterator<Entry<K, V>> entryIterator) {
            this.asm = asm;
            this.entryIterator = entryIterator;
        }


        @Override
        public Comparator<? super Entry<K, V>> comparator() {
            return Comparator.comparing(Entry::getKey, asm.comparator());
        }

        @Override
        public int size() {
            return asm.size();
        }

        @Override
        public boolean isEmpty() {
            return asm.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return asm.containsKey(o);
        }

        @Override
        public boolean add(Entry<K, V> entry) {
            V val = entry.getValue();
            return !Objects.equals(asm.put(entry.getKey(), val), val);
        }

        @Override
        public boolean remove(Object t) {
            return asm.remove(t) != null;
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return entryIterator;
        }
    }
}
