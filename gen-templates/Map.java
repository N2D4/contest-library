import java.util.NoSuchElementException;
import java.util.Set;

/* GENERIFY-THIS */
public interface Map<K, V> {
    /**
     * Returns the value associated with this key, or throws an exception if it can't be retrieved. (Note that this
     * differs from usual hash map behaviour.)
     */
    V get(K key) throws NoSuchElementException;
    default V getOrDefault(K key, V def) {
        return containsKey(key) ? get(key) : def;
    }
    boolean containsKey(K key);
    V set(K key, V value);
    int size();
    Set<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();
        V getValue();
        V setValue(V val);
    }
}