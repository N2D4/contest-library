import lib.algorithms.PrimeUtils;
import lib.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTests {

    @Test
    public void indexOfTests() {
        String[][] testcases = {
            {"12", "123"},
            {"12", ""},
            {"aba ababacuas aaababcuas abababacuabacuas", "abacuas"},
            {"Greetings", "ee"},
            {"aba ababaacus", "ababacus"},
            {"aba abababacus", "ababacus"},
        };

        for (String[] testcase : testcases) {
            String s = testcase[0];
            String sub = testcase[1];
            for (int i = 0; i < s.length(); i++) {
                assertEquals(s.indexOf(sub, i), StringUtils.indexOf(s, sub, i));
            }
        }
    }



    @Test
    public void primeFactorTests() {
        Random random = new Random("random prime factor tests".hashCode());
        for (long i = 0; i < 10 * TestConstants.SCALE; i++) {
            long l = random.nextDouble() < 0.1 ? BigInteger.probablePrime(45, random).longValue() : getPositiveLong(random);
            long p = 1;
            for (long f : PrimeUtils.findPrimeFactors(l)) {
                assertTrue(PrimeUtils.isPrime(f));
                p *= f;
            }
            assertEquals(l, p);
        }
    }


    private long getPositiveLong(Random rnd) {
        while (true) {
            long l = rnd.nextLong();
            if (l > 0) {
                return l;
            }
        }
    }

}
