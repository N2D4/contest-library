/* Generated by Generificator from Map.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import java.util.NoSuchElementException;
import java.util.Set;


public interface DoubleObjMap<Obj1__> {
    /**
     * Returns the value associated with this key, or throws an exception if it can't be retrieved. (Note that this
     * differs from usual hash map behaviour.)
     */
    Obj1__ get(double key) throws NoSuchElementException;
    default Obj1__ getOrDefault(double key, Obj1__ def) {
        return containsKey(key) ? get(key) : def;
    }
    boolean containsKey(double key);
    Obj1__ set(double key, Obj1__ value);
    int size();
    Set<Entry<Obj1__>> entrySet();

    interface Entry<Obj1__> {
        double getKey();
        Obj1__ getValue();
        Obj1__ setValue(Obj1__ val);
    }
}