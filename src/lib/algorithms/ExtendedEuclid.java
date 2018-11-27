package lib.algorithms;

import lib.utils.tuples.Triple;

public final class ExtendedEuclid extends Algorithm {
    private ExtendedEuclid() {
        // Quite dusty here...
    }


    public static Triple<Long, Long, Long> extendedEuclid(long a, long b) {
        if (b == 0) {
            return new Triple<>(a, 1l, 0l);
        }

        Triple<Long, Long, Long> res = extendedEuclid(b, a % b);
        long tmp = res.b;
        res.b = res.c;
        res.c = tmp - a / b * res.b;

        return res;
    }

    public static long greatestCommonDivisor(long a, long b) {
        return gcd(a, b);
    }

    public static long leastCommonMultiple(long a, long b) {
        return lcm(a, b);
    }

    public static long gcd(long a, long b) {
        return extendedEuclid(a, b).a;
    }

    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    public static long modularInverse(long a, long modulo) {
        Triple<Long, Long, Long> result = extendedEuclid(a, modulo);
        if (result.a != 1) throw new IllegalArgumentException("Arguments are not relatively prime!");
        return result.b;
    }
}
