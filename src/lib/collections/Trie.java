package lib.collections;

import lib.trees.Tree;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.function.DoubleBinaryOperator;

public class Trie<T> extends AbstractSortedMap<String, T> {
    private Tree tree = new Tree<>((TrieData<T>) null);

    public Trie() {

    }

    @Override
    public T get(Object key) {
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public T put(String key, T value) {
        return null;
    }

    @Override
    public T remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Entry<String, T>> entryIterator() {
        return null;
    }

    private static class TrieData<T> {

    }
}
