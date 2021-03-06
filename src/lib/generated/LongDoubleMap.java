/* Generated by Generificator from Map.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.NoSuchElementException;
import java.util.Set;


public interface LongDoubleMap {
    /**
     * Returns the value associated with this key, or throws an exception if it can't be retrieved. (Note that this
     * differs from usual hash map behaviour.)
     */
    double get(long key) throws NoSuchElementException;
    default double getOrDefault(long key, double def) {
        return containsKey(key) ? get(key) : def;
    }
    boolean containsKey(long key);
    double set(long key, double value);
    int size();
    Set<Entry> entrySet();

    interface Entry {
        long getKey();
        double getValue();
        double setValue(double val);
    }
}