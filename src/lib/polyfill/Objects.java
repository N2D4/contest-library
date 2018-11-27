/* BEGIN-POLYFILL-6 *../

package lib.polyfill;

import java.util.Arrays;

public final class Objects {
    private Objects() {
        // Quite dusty here...
    }

    public static int hashCode(Object obj) {
        if (obj == null) return 0;
        else return obj.hashCode();
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static boolean equals(Object a, Object b) {
        if (a == b) return true;
        if (a == null) return false;
        return a.equals(b);
    }

}

/..* END-POLYFILL-6 */