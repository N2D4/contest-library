package lib.algorithms;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PrimeUtils extends Algorithm {

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

}
