package lib.utils.tuples;

import lib.utils.various.Structure;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Tuple extends Structure {

    public abstract Object[] toArray();


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Tuple)) return false;

        return Arrays.equals(this.toArray(), ((Tuple) obj).toArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.toArray());
    }

    @Override
    public String toString() {
        Object[] els = toArray();
        String[] elements = new String[els.length];
        for (int i = 0; i < els.length; i++) {
            elements[i] = "" + els[i];
        }
        return "(" + String.join(", ", elements) + ")";
    }
}
