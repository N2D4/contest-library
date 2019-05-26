import lib.utils.PrimeUtils;
import lib.utils.StringUtils;
import lib.utils.tuples.*;
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

    @Test
    public void tupleEqualsTest() {
        Random random = new Random("tuple equality test".hashCode());
        for (long i = 0; i < 1000 * TestConstants.SCALE; i++) {
            Pair<Integer, Integer> pair = new Pair<>(random.nextInt(500), random.nextInt(500));
            assertEquals(pair, new ArrayTuple(new Object[] {pair.a, pair.b}));
            assertEquals(pair.hashCode(), new ArrayTuple(new Object[] {pair.a, pair.b}).hashCode());
            assertNotEquals(pair, new ArrayTuple(new Object[] {pair.a + 1, pair.b}));
            assertNotEquals(pair, new ArrayTuple(new Object[] {pair.a, pair.b + 1}));

            Monad<Integer> monad = new Monad<>(random.nextInt());
            assertEquals(monad, new ArrayTuple(new Object[] {monad.value}));
            assertEquals(monad.hashCode(), new ArrayTuple(new Object[] {monad.value}).hashCode());
            assertNotEquals(monad, new ArrayTuple(new Object[] {monad.value + 1}));

            pair = new Pair<>(random.nextInt(), random.nextInt());
            assertEquals(pair, new ArrayTuple(new Object[] {pair.a, pair.b}));
            assertEquals(pair.hashCode(), new ArrayTuple(new Object[] {pair.a, pair.b}).hashCode());
            assertNotEquals(pair, new ArrayTuple(new Object[] {pair.a + 1, pair.b}));
            assertNotEquals(pair, new ArrayTuple(new Object[] {pair.a, pair.b + 1}));

            Triple<Integer, Integer, Integer> triple = new Triple<>(random.nextInt(), random.nextInt(), random.nextInt());
            assertEquals(triple, new ArrayTuple(new Object[] {triple.a, triple.b, triple.c}));
            assertEquals(triple.hashCode(), new ArrayTuple(new Object[] {triple.a, triple.b, triple.c}).hashCode());
            assertNotEquals(triple, new ArrayTuple(new Object[] {triple.a + 1, triple.b, triple.c}));
            assertNotEquals(triple, new ArrayTuple(new Object[] {triple.a, triple.b + 1, triple.c}));
            assertNotEquals(triple, new ArrayTuple(new Object[] {triple.a, triple.b, triple.c + 1}));

            Quadruple<Integer, Integer, Integer, Integer> quadruple = new Quadruple<>(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
            assertEquals(quadruple, new ArrayTuple(new Object[] {quadruple.a, quadruple.b, quadruple.c, quadruple.d}));
            assertEquals(quadruple.hashCode(), new ArrayTuple(new Object[] {quadruple.a, quadruple.b, quadruple.c, quadruple.d}).hashCode());
            assertNotEquals(quadruple, new ArrayTuple(new Object[] {quadruple.a + 1, quadruple.b, quadruple.c, quadruple.d}));
            assertNotEquals(quadruple, new ArrayTuple(new Object[] {quadruple.a, quadruple.b + 1, quadruple.c, quadruple.d}));
            assertNotEquals(quadruple, new ArrayTuple(new Object[] {quadruple.a, quadruple.b, quadruple.c + 1, quadruple.d}));
            assertNotEquals(quadruple, new ArrayTuple(new Object[] {quadruple.a, quadruple.b, quadruple.c, quadruple.d + 1}));
        }
    }

    private class ArrayTuple extends Tuple {
        private final Object[] array;

        ArrayTuple(Object[] array) {
            this.array = array;
        }

        @Override
        public Object[] toArray() {
            return array;
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
