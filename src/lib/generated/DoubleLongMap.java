/* Generated by Generificator from Map.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.NoSuchElementException;
import java.util.Set;


public interface DoubleLongMap {
    /**
     * Returns the value associated with this key, or throws an exception if it can't be retrieved. (Note that this
     * differs from usual hash map behaviour.)
     */
    long get(double key) throws NoSuchElementException;
    default long getOrDefault(double key, long def) {
        return containsKey(key) ? get(key) : def;
    }
    boolean containsKey(double key);
    long set(double key, long value);
    int size();
    Set<Entry> entrySet();

    interface Entry {
        double getKey();
        long getValue();
        long setValue(long val);
    }
}