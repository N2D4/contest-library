package lib.utils.tuples;

import lib.utils.various.Structure;

import java.util.Arrays;

public abstract class Tuple extends Structure {

    @Override
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
        /* BEGIN-JAVA-8 */
        Object[] els = toArray();
        String[] elements = new String[els.length];
        for (int i = 0; i < els.length; i++) {
            elements[i] = els[i].toString();
        }
        return "(" + String.join(", ", elements) + ")";
        /* END-JAVA-8 */
        /* BEGIN-POLYFILL-6 *../
        return "Not supported in Java 6 mode!";
        /..* END-POLYFILL-6 */
    }
}
