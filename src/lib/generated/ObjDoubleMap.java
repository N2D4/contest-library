/* Generated by Generificator on Mon Dec 02 16:10:30 CET 2019 from Map.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.NoSuchElementException;
import java.util.Set;


public interface ObjDoubleMap<Obj1__> {
    /**
     * Returns the value associated with this key, or throws an exception if it can't be retrieved. (Note that this
     * differs from usual hash map behaviour.)
     */
    double get(Obj1__ key) throws NoSuchElementException;
    default double getOrDefault(Obj1__ key, double def) {
        return containsKey(key) ? get(key) : def;
    }
    boolean containsKey(Obj1__ key);
    double set(Obj1__ key, double value);
    int size();
    Set<Entry<Obj1__>> entrySet();

    interface Entry<Obj1__> {
        Obj1__ getKey();
        double getValue();
        double setValue(double val);
    }
}