public abstract class AbstractMap<K, V> implements Map<K, V> {
    // TODO equals, hashcode

    @Override
    public String toString() {
        return entrySet().toString();
    }
}
