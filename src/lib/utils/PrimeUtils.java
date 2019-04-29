package lib.utils;

import lib.algorithms.Algorithm;

import java.math.*;
import java.util.*;

public final class PrimeUtils {

    public static boolean isProbablePrime(long l, int certainty) {
        return BigInteger.valueOf(l).isProbablePrime(certainty);
    }

    public static boolean isPrime(long l) {
        return isProbablePrime(l, 100);
    }

    public static long findPrimeFactor(long l) {
        return findSmallestOddFactor(l, 1, l);
    }

    private static long findSmallestOddFactor(long l, long min, long max) {
        if (min <= 2) {
            if (l % 2 == 0) return 2;
            min = 3;
        }
        max = Math.min(max, (long) Math.sqrt(l) + 1);
        for (long i = min; i < max; i += 2) {
            if (l % i == 0) return i;
        }
        return l;
    }

    public static List<Long> findPrimeFactors(long l) {
        return findPrimeFactors(l, l);
    }

    public static List<Long> findPrimeFactors(long l, long max) {
        if (PrimeUtils.isPrime(l)) return new ArrayList<>(Arrays.asList(l));

        List<Long> result = new ArrayList<>();
        long pf = 1;
        while (l > 1 && l <= max) {
            pf = findSmallestOddFactor(l, pf, l);
            result.add(pf);
            l /= pf;
        }
        return result;
    }

    public static boolean isProbablePrime(BigInteger l, int certainty) {
        return l.isProbablePrime(certainty);
    }

    public static boolean isPrime(BigInteger l) {
        return isProbablePrime(l, 100);
    }

    public static BigInteger findPrimeFactor(BigInteger l) {
        return findSmallestOddFactor(l, BigInteger.ONE, l);
    }

    private static BigInteger findSmallestOddFactor(BigInteger l, BigInteger min, BigInteger max) {
        if (min.compareTo(BigInteger.valueOf(2)) <= 0) {
            if (l.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) return BigInteger.valueOf(2);
            min = BigInteger.valueOf(3);
        }
        for (BigInteger i = min; i.compareTo(max) < 0 && i.multiply(i).compareTo(l) <= 0; i = i.add(BigInteger.valueOf(2))) {
            if (l.mod(i).equals(BigInteger.ZERO)) return i;
        }
        return l;
    }

    public static List<BigInteger> findPrimeFactors(BigInteger l) {
        return findPrimeFactors(l, l);
    }

    public static List<BigInteger> findPrimeFactors(BigInteger l, BigInteger max) {
        if (PrimeUtils.isPrime(l)) return new ArrayList<>(Arrays.asList(l));

        List<BigInteger> result = new ArrayList<>();
        BigInteger pf = BigInteger.ONE;
        while (l.compareTo(BigInteger.ONE) > 0 && l.compareTo(max) <= 0) {
            pf = findSmallestOddFactor(l, pf, l);
            result.add(pf);
            l = l.divide(pf);
        }
        return result;
    }

}
