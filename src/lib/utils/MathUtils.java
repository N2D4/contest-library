package lib.utils;

/* BEGIN-JAVA-8 */
import java.util.function.IntBinaryOperator;
/* END-JAVA-8 */

public final class MathUtils {
    private MathUtils() {
        // Quite dusty here...
    }

    public static int[] splitIntoArray(int i, int[] max) {
        int[] res = new int[max.length];
        for (int j = max.length - 1; j >= 0; j--) {
            res[j] = i % max[j];
            i /= max[j];
        }
        return res;
    }

    public static int mergeIntoInteger(int[] arr, int[] max) {
        int result = 0;
        for (int i = 0; i < max.length; i++) {
            result *= max[i];
            result += arr[i];
        }
        return result;
    }

    /* BEGIN-JAVA-8 */
    public static int foldl(IntBinaryOperator operator, int... vals) {
        int result = vals[0];
        for (int i = 1; i < vals.length; i++) {
            result = operator.applyAsInt(result, vals[i]);
        }
        return result;
    }

    public static int min(int... vals) {
        return foldl(Math::min, vals);
    }

    public static int max(int... vals) {
        return foldl(Math::max, vals);
    }
    /* END-JAVA-8 */

    public static int sum(int... vals) {
        int result = 0;
        for (int v : vals) {
            result += v;
        }
        return result;
    }

    public static int prod(int... vals) {
        int result = 1;
        for (int v : vals) {
            result *= v;
        }
        return result;
    }

    public static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    public static boolean doubleEquals(double a, double b) {
        return a == b || (Double.isNaN(a) && Double.isNaN(b));
    }


    public static int sq(int a) {
        return a*a;
    }

    public static long sq(long a) {
        return a*a;
    }

    public static double sq(double a) {
        return a*a;
    }
}
