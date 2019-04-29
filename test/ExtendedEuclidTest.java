import lib.algorithms.ExtendedEuclid;
import lib.utils.MathUtils;
import lib.utils.tuples.Pair;
import lib.utils.tuples.Triple;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtendedEuclidTest {


    @Test
    public void extendedEuclid() {
        Random random = new Random("extended euclid test".hashCode());
        for (int i = 0; i < 100_000 * TestConstants.SCALE; i++) {
            long a = random.nextInt();
            long b = random.nextInt();
            if (random.nextBoolean()) {
                a = random.nextLong();
                b = random.nextLong();
            }
            a = cabs(a);
            b = cabs(b);

            Triple<Long, Long, Long> res = ExtendedEuclid.extendedEuclid(a, b);
            assertEquals((long) res.a, res.b * a + res.c * b);
            assertEquals(0, a % res.a);
            assertEquals(0, b % res.a);
            if (a <= Integer.MAX_VALUE && b <= Integer.MAX_VALUE) {
                long lcm = ExtendedEuclid.lcm(a, b);
                assertEquals(0, lcm % a);
                assertEquals(0, lcm % b);
                assertTrue((res.a != 1 && lcm < a * b) || (res.a == 1 && lcm == a * b));
            }
        }
    }

    @Test
    public void chineseRemainderTheorem() {
        Random random = new Random("modular math test".hashCode());
        for (int i = 0; i < 100000 * TestConstants.SCALE; i++) {
            long b = random.nextLong();
            int count = random.nextInt(Math.min(i + 1, 10)) + 2;
            long[] m = new long[count];
            long[] a = new long[count];
            for (int j = 0; j < count; j++) {
                m[j] = random.nextInt(Math.min(10 * i + 10, 50)) + 1;
                a[j] = MathUtils.realMod(b, m[j])/* + (random.nextInt(11) - 5) * m[j]*/;
            }
            Pair<Long, Long> res = ExtendedEuclid.chineseRemainderTheorem(a, m);
            assertEquals(MathUtils.realMod(b, res.b), (long) res.a);
        }
    }


    private static long cabs(long l) {
        return Math.max(Math.abs(l), 1);
    }
}
