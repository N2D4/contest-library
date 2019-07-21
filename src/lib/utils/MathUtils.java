package lib.utils;

/* BEGIN-JAVA-8 */
import java.math.BigInteger;
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

    public static int pow(int a, int exponent) {
        if (exponent < 0) return 0;
        if (exponent == 0) return 1;
        if ((a & (a - 1)) == 0) return a << (exponent - 1);
        if (exponent % 2 == 0) return sq(pow(a, exponent / 2));
        return pow(a, exponent - 1) * a;
    }

    public static long pow(long a, long exponent) {
        if (a == 2) return 1l << exponent;
        if (exponent < 0) return 0;
        if (exponent == 0) return 1;
        if (exponent % 2 == 0) return sq(pow(a, exponent / 2));
        return pow(a, exponent - 1) * a;
    }

    public static double pow(double a, double exponent) {
        return Math.pow(a, exponent);
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


    /**
     * Real modulo (never returns a negative number). Same as floorMod
     */
    public static int realMod(int i, int mod) {
        return MathUtils.floorMod(i, mod);
    }

    /**
     * Real modulo (never returns a negative number). Same as floorMod
     */
    public static long realMod(long i, long mod) {
        return MathUtils.floorMod(i, mod);
    }

    public static int floorDiv(int a, int b) {
        return Math.floorDiv(a, b);
    }

    public static long floorDiv(long a, long b) {
        return Math.floorDiv(a, b);
    }

    public static int floorMod(int a, int b) {
        return Math.floorMod(a, b);
    }

    public static long floorMod(long a, long b) {
        return Math.floorMod(a, b);
    }

    public static int ceilDiv(int a, int b) {
        return Math.floorDiv(a + b - 1, b);
    }

    public static long ceilDiv(long a, long b) {
        return Math.floorDiv(a + b - 1, b);
    }

    public static int ceilMod(int a, int b) {
        return a - ceilDiv(a, b) * b;
    }

    public static long ceilMod(long a, long b) {
        return a - ceilDiv(a, b) * b;
    }

    /**
     * Overflow-aware modular multiplication. Never overflows
     */
    public static long modMul(long a, long b, long mod) {
        a = realMod(a, mod);
        if (a == 0) return 0;
        b = realMod(b, mod);
        if (b <= Long.MAX_VALUE / a) {
            return MathUtils.realMod(a * b, mod);
        }
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValueExact();
    }

    /**
     * Overflow-aware modular addition. Never overflows
     */
    public static long modAdd(long a, long b, long mod) {
        a = realMod(a, mod);
        b = realMod(b, mod);
        return realMod(a - mod + b, mod);
    }
}
