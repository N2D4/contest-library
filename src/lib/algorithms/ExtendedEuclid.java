package lib.algorithms;

import lib.utils.MathUtils;
import lib.utils.tuples.Pair;
import lib.utils.tuples.Triple;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;

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
        return Math.multiplyExact(a / gcd(a, b), b);
    }

    public static long modularInverse(long a, long modulo) {
        Triple<Long, Long, Long> result = extendedEuclid(MathUtils.realMod(a, modulo), modulo);
        if (result.a != 1) throw new IllegalArgumentException("Arguments are not coprime!");
        return result.b;
    }

    public static Pair<Long, Long> chineseRemainderTheorem(int[] a, int[] m) {
        return chineseRemainderTheorem(IntStream.range(0, a.length).mapToObj(i -> new Pair<>(a[i], m[i])).toArray(Pair[]::new));
    }

    public static Pair<Long, Long> chineseRemainderTheorem(long[] a, long[] m) {
        return chineseRemainderTheorem(IntStream.range(0, a.length).mapToObj(i -> new Pair<>(a[i], m[i])).toArray(Pair[]::new));
    }

    public static Pair<Long, Long> chineseRemainderTheorem(Pair<Long, Long> ...equations) {
        if (equations.length == 2) {
            if (Arrays.asList(equations).contains(null)) return null;
            long a = equations[0].a;
            long m = equations[0].b;
            long b = equations[1].a;
            long n = equations[1].b;

            Triple<Long, Long, Long> euclid = extendedEuclid(m, n);
            long T = euclid.a;                        // gcd
            long M = Math.multiplyExact(m / T, n);    // lcm

            if (MathUtils.realMod(a, T) != MathUtils.realMod(b, T)) return null; // no solutions

            a = MathUtils.realMod(a, m);
            b = MathUtils.realMod(b, n);

            long u = euclid.b;
            long v = euclid.c;


            BigInteger bia = BigInteger.valueOf(a);
            BigInteger bib = BigInteger.valueOf(b);
            BigInteger biu = BigInteger.valueOf(u);
            BigInteger biv = BigInteger.valueOf(v);
            BigInteger bim = BigInteger.valueOf(m);
            BigInteger bin = BigInteger.valueOf(n);
            BigInteger biM = BigInteger.valueOf(M);
            BigInteger biT = BigInteger.valueOf(T);
            long res = MathUtils.realMod(bia.multiply(biv).multiply(bin).add(bib.multiply(biu).multiply(bim)).divide(biT).mod(biM).longValueExact(), M);
            return new Pair<>(res, M);
        } else {
            return Arrays.stream(equations).reduce(new Pair<>(0l, 1l), ExtendedEuclid::chineseRemainderTheorem);
        }
    }

    private static long trimodmul(long a, long b, long c, long mod) {
        return MathUtils.modMul(MathUtils.modMul(a, b, mod), c, mod);
    }
}
