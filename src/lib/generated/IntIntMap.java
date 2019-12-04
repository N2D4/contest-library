/* Generated by Generificator on Wed Dec 04 00:49:20 CET 2019 from Map.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.NoSuchElementException;
import java.util.Set;


public interface IntIntMap {
    /**
     * Returns the value associated with this key, or throws an exception if it can't be retrieved. (Note that this
     * differs from usual hash map behaviour.)
     */
    int get(int key) throws NoSuchElementException;
    default int getOrDefault(int key, int def) {
        return containsKey(key) ? get(key) : def;
    }
    boolean containsKey(int key);
    int set(int key, int value);
    int size();
    Set<Entry> entrySet();

    interface Entry {
        int getKey();
        int getValue();
        int setValue(int val);
    }
}