package lib.utils.various;

import lib.utils.tuples.Tuple;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class Structure implements Serializable, Cloneable {

    @Override
    public Structure clone() {
        try {
            Structure struct = (Structure) super.clone();
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                field.set(struct, field.get(this));
            }
            return struct;
        } catch (CloneNotSupportedException e) {
            // Cloning a structure is always supported
            /* BEGIN-JAVA-8 */
            throw new AssertionError("Cloning a structure should always be supported - please report this bug", e);
            /* END-JAVA-8 */
            /* BEGIN-POLYFILL-6 *../
            throw new AssertionError("Cloning a structure should always be supported - please report this bug");
            /..* END-POLYFILL-6 */
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't access my own elements!", e);
        }
    }


    private NavigableMap<String, Object> getFields() {
        try {
            NavigableMap<String, Object> result = new TreeMap<String, Object>();
            for (Field field : this.getClass().getFields()) {
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                result.put(field.getName(), field.get(this));
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't access my own elements!", e);
        }
    }



    public Object[] toArray() {
        NavigableMap<String, Object> map = getFields();
        Object[] arr = new Object[map.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            arr[i] = entry.getValue();
        }
        return arr;
    }

    public <T> T[] toArray(T[] t) {
        Object[] arr = toArray();
        if (t.length < arr.length) {
            return (T[]) Arrays.copyOf(arr, arr.length, t.getClass());
        }

        System.arraycopy(arr, 0, t, 0, arr.length);
        if (t.length > arr.length)
            t[arr.length] = null;
        return t;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        return getFields().equals(((Structure) obj).getFields());
    }

    @Override
    public int hashCode() {
        return getFields().hashCode();
    }


    @Override
    public String toString() {
        return getFields().toString();
    }


}
