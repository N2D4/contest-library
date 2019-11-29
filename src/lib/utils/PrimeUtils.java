package lib.utils;

import lib.algorithms.BinarySearch;
import lib.collections.HashMultiset;
import lib.collections.Multiset;
import lib.generated.*;
import lib.utils.various.LongRange;
import lib.utils.various.Range;

import java.math.*;
import java.util.*;
import java.util.stream.Collectors;

public final class PrimeUtils {
    private static final int CACHE_IF_IN_DISTANCE = 10_000;
    private static final double CACHE_SCALE_FACTOR = 1.5;
    private static final long BIG_INTEGER_THRESHOLD = (1l << 61l) + 10_000;

    //region Caching
    private static IntList cachedPrimes = new IntArrayList(new int[] {2, 3, 5, 7, 11, 13, 17, 19});
    private static int nextUncached = cachedPrimes.get(cachedPrimes.size() - 1) + 1; // or Integer.MIN_VALUE if cachedPrimes.last == Integer.MAX_VALUE

    public static int getCacheSize() {
        return cachedPrimes.size();
    }

    private static void ensureCached(int untilInclusive) {
        if (nextUncached < 0 || nextUncached > untilInclusive) return;

        untilInclusive = MathUtils.max(untilInclusive, untilInclusive + 200);
        if (untilInclusive == Integer.MAX_VALUE) {
            ensureCached(untilInclusive - 1);
            cachedPrimes.add(Integer.MAX_VALUE);
            nextUncached = Integer.MIN_VALUE;
            return;
        }

        getCacheUnattachedPrimeIterator(new LongRange(nextUncached, untilInclusive + 1)).forEachRemaining(l -> cachedPrimes.add((int) l));

        nextUncached = untilInclusive + 1;
    }

    private static void enlargenCache(int max) {
        if (nextUncached < 0) return;
        double nu = (nextUncached * CACHE_SCALE_FACTOR);
        if (nu > Integer.MAX_VALUE) nu = Integer.MAX_VALUE;
        ensureCached(Math.min((int) nu, max));
    }

    private static boolean isCacheFeasible(long start) {
        return start - nextUncached < CACHE_IF_IN_DISTANCE; // subtraction to make this work even if nextUncached == Integer.MIN_VALUE
    }
    //endregion

    //region getPrimes()
    public static LongIterator getPrimeIterator(LongRange range) {
        if (range.size() <= 0) {
            // empty iterator
            return new LongIterator() {
                @Override
                public long next() {
                    throw new NoSuchElementException();
                }

                @Override
                public boolean hasNext() {
                    return false;
                }
            };
        } else if (isCacheFeasible(range.a) && range.b <= Integer.MAX_VALUE) {
            // cache-attached iterator
            // range.b should generally not change, we do a performance hack in findPrimeFactors which requires this function to behave like this when range.b changes
            return new LongIterator() {
                int a = Math.toIntExact(range.a);
                int b = Math.toIntExact(range.b);
                int indx = a <= 2 ? 0 : a == 3 ? 1 : BinarySearch.searchInRange(new Range(0, cachedPrimes.size()), i -> cachedPrimes.get(i) >= a);

                @Override
                public long next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return cachedPrimes.get(indx++);
                }

                @Override
                public boolean hasNext() {
                    while (indx >= cachedPrimes.size()) {
                        enlargenCache(Math.max(1 + nextUncached, 1 + Math.min(b, Math.toIntExact(range.b))));
                    }
                    return indx < cachedPrimes.size() && cachedPrimes.get(indx) < b;
                }
            };
        } else {
            // cache-unattached iterator
            return getCacheUnattachedPrimeIterator(range);
        }
    }

    private static LongIterator getCacheUnattachedPrimeIterator(LongRange range) {
        boolean[] table = getUncachedPrimeTable(range);
        return new LongIterator() {
            long i = range.a;

            @Override
            public long next() {
                if (!hasNext()) throw new NoSuchElementException();
                return i++;
            }

            @Override
            public boolean hasNext() {
                while (true) {
                    if (i >= range.b) return false;
                    if (table[(int) (i-range.a)]) return true;
                    i++;
                }
            }
        };
    }

    public static LongList getPrimesUntil(int endExclusive) {
        return getPrimesIn(new LongRange(2, endExclusive));
    }

    public static LongList getPrimesIn(LongRange range) {
        LongList result = new LongArrayList();
        LongIterator it = getPrimeIterator(range);
        it.forEachRemaining(result::add);
        return result;
    }
    //endregion

    //region getPrimeTable()
    /**
     * Returns a boolean array, where arr[i] means that range.a + i is a prime number.
     */
    public static boolean[] getPrimeTable(Range range) {
        return getPrimeTable(range.toLongRange());
    }

    /**
     * Returns a boolean array where arr[i] is true iff `range.a + i` is a prime number. `range.b - range.a` should be
     * small (fit in an integer).
     */
    public static boolean[] getPrimeTable(LongRange range) {
        if (range.size() >= Integer.MAX_VALUE) throw new IllegalArgumentException("Distance between L and R may not exceed Integer.MAX_VALUE!");

        return getUncachedPrimeTable(range);
    }

    private static boolean[] getUncachedPrimeTable(LongRange range) {
        long L = range.a;
        long R = range.b - 1;

        boolean[] primeFacts = new boolean[(int) (R - L + 1)];
        for (int i = (int) (~L & 1); i < primeFacts.length; i += 2) {
            primeFacts[i] = true;
        }
        if (L == 0 && R >= 0) primeFacts[0] = false;
        if (L <= 1 && R >= 1) primeFacts[(int) (1 - L)] = false;
        if (L <= 2 && R >= 2) primeFacts[(int) (2 - L)] = true;

        LongIterator primes = getPrimeIterator(new LongRange(3, (long) (Math.sqrt(range.b) + 1)));

        while (primes.hasNext()) {
            long i = primes.next();
            for (long j = Math.max(i, MathUtils.ceilDiv(L, i)); j * i <= R; j++) {
                primeFacts[(int) (j * i - L)] = false;
            }
        }

        return primeFacts;
    }
    //endregion

    //region isProbablePrime()
    /**
     * Returns true if there is at most a 1:2^certainty chance that this number is composite, or false if it is
     * definitely composite.
     */
    public static boolean isProbablePrime(long l, int certainty) {
        return BigInteger.valueOf(l).isProbablePrime(certainty);
    }

    /**
     * Returns true if there is at most a 1:2^certainty chance that this number is composite, or false if it is
     * definitely composite.
     */
    public static boolean isProbablePrime(BigInteger l, int certainty) {
        return l.isProbablePrime(certainty);
    }
    //endregion

    //region isPrime()
    /**
     * Returns true if this number is prime, false otherwise.
     */
    public static boolean isPrime(long l) {
        return isProbablePrime(l, 100);
    }

    /**
     * Returns true if this number is prime, false otherwise.
     */
    public static boolean isPrime(BigInteger l) {
        return isProbablePrime(l, 100);
    }
    //endregion

    //region findPrimeFactor[s]()
    /**
     * Returns the smallest prime factor of l.
     */
    public static long findPrimeFactor(long l) {
        return findSmallestOddFactor(l, 1, l);
    }

    /**
     * Returns 2 if l is divisible by 2, or the smallest odd (possibly composite) factor in [min, max]. If no such
     * factor exists, return l (even if l > max).
     */
    private static long findSmallestOddFactor(long l, long min, long max) {
        if (min <= 2) {
            if (l % 2 == 0) return 2;
            min = 3;
        }
        max = Math.min(max, (long) Math.sqrt(l) + 1);
        for (long i = min; i <= max; i += 2) {
            if (l % i == 0) return i;
        }
        return l;
    }

    public static List<Long> findPrimeFactors(long l) {
        return findPrimeFactors(l, l);
    }

    /**
     * Returns all prime factors of l in the range [0, max].
     */
    public static List<Long> findPrimeFactors(long l, long max) {
        if (l > BIG_INTEGER_THRESHOLD) return findPrimeFactors(BigInteger.valueOf(l)).stream().map(BigInteger::longValueExact).collect(Collectors.toList());

        if (l <= 1) return Collections.emptyList();
        int sqrt = (int) Math.sqrt(l);
        if (sqrt*sqrt == max) {
            List<Long> res = new ArrayList<>();
            findPrimeFactors(sqrt, max).forEach(i -> {
                res.add(i);
                res.add(i);
            });
            return Collections.unmodifiableList(res);
        }
        if (l >= 10_000_000 && PrimeUtils.isPrime(l)) return l > max ? Collections.emptyList() : Collections.singletonList(l);


        List<Long> result = new ArrayList<>();
        LongRange range = new LongRange(0, 1 + Math.min(1 + sqrt, max));
        LongIterator it = getPrimeIterator(range);
        while (it.hasNext()) {
            long p = it.next();
            if (p * p > l) break;
            while (l % p == 0) {
                result.add(p);
                l /= p;
                range.b = l;
            }
        }
        if (l <= max && l > 1) result.add(l);
        return Collections.unmodifiableList(result);
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
        if (l.compareTo(BigInteger.valueOf(BIG_INTEGER_THRESHOLD)) < 0) return findPrimeFactors(l.longValueExact()).stream().map(BigInteger::valueOf).collect(Collectors.toList());
        if (PrimeUtils.isPrime(l)) return Collections.singletonList(l);

        List<BigInteger> result = new ArrayList<>();
        BigInteger pf = BigInteger.ONE;
        while (l.compareTo(BigInteger.ONE) > 0 && l.compareTo(max) <= 0) {
            pf = findSmallestOddFactor(l, pf, max);
            if (pf.compareTo(max) > 0) break;
            result.add(pf);
            l = l.divide(pf);
        }
        return result;
    }
    //endregion

    //region eulerTotient()
    public static long eulerTotient(long l) {
        return eulerTotient(findPrimeFactors(l));
    }

    public static long eulerTotient(Collection<Long> primeFactors) {
        return eulerTotient(new HashMultiset<>(primeFactors));
    }

    public static long eulerTotient(Multiset<Long> primeFactors) {
        return primeFactors.entryStream()
                .mapToLong(a -> (a.a - 1) * MathUtils.pow(a.a, (a.b - 1)))
                .reduce(1, (a, b) -> a * b);
    }

    public static BigInteger eulerTotientBI(BigInteger l) {
        return eulerTotientBI(findPrimeFactors(l));
    }

    public static BigInteger eulerTotientBI(Collection<BigInteger> primeFactors) {
        return eulerTotientBI(new HashMultiset<>(primeFactors));
    }

    public static BigInteger eulerTotientBI(Multiset<BigInteger> primeFactors) {
        return primeFactors.entryStream()
                .map(a -> a.a.subtract(BigInteger.ONE).multiply(a.a.pow(a.b - 1)))
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }
    //endregion

}
