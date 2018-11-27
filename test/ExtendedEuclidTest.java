import lib.algorithms.ExtendedEuclid;
import lib.graphs.*;
import lib.graphs.algorithms.FloydWarshall;
import lib.graphs.algorithms.GraphSearch;
import lib.utils.MathUtils;
import lib.utils.tuples.Triple;
import lib.utils.various.PartitionList;
import lib.utils.various.Range;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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


    private static long cabs(long l) {
        return Math.max(Math.abs(l), 1);
    }
}
