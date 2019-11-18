import lib.utils.Utils;

import java.util.Set;

public class HashMap<K, V> extends AbstractMap<K, V> {
    private static final double loadFactor = 1.25;
    private static final int valuesPerBucket = 4;

    private HashMap<K, V> nextMap = null;
    private int size = 0;
    private int boxesAnd;
    private /*BOX K*/Object[] keys;
    private /*BOX V*/Object[] values;

    @Override
    public V get(K key) {
        return get(hash(key), key);
    }

    private V get(int hash, K key) {
        int index = Utils.hashCode(key) & boxesAnd;
        for (int i = 0; i < valuesPerBucket; i++) {
            if (Utils.equals(keys[index+i], key)) {
                return (V) values[index+i];
            }
        }
        throw new RuntimeException("unimplemented");
    }

    @Override
    public V getOrDefault(K key, V def) {
        return getOrDefault(hash(key), key, def);
    }

    public boolean containsKey(int hash, K key) {
        throw new RuntimeException("unimplemented");
    }

    @Override
    public boolean containsKey(K key) {
        throw new RuntimeException("unimplemented");
    }

    private V getOrDefault(int hash, K key, V def) {
        return containsKey(hash, key) ? get(hash, key) : def;
    }

    @Override
    public V set(K key, V value) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private int hash(K key) {
        return Utils.hashCode(key);
    }
}
