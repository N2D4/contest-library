package lib.utils.various;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * A class whose sub-classes automatically support serialization, cloning, hashing and equality. These methods are
 * defined field-wise, whereas transient fields are ignored (but private non-transient fields will also be serialized.)
 */
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
            throw new AssertionError("Cloning a structure should always be supported - please report this bug", e);
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
