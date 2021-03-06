import lib.algorithms.BinarySearch;
import lib.utils.ArrayUtils;
import lib.utils.tuples.Pair;
import lib.utils.various.Range;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinarySearchTests {

    @Test
    public void intArrayBinarySearch() {
        Random random = new Random("int array binary search test".hashCode());
        for (int it = 0; it < 1000 * TestConstants.SCALE; it++) {
            int size = random.nextInt(it <= 25 ? it + 1 : 100);
            int bound = random.nextInt(random.nextBoolean() ? size + 1 : 2_000_000_000) + 1;

            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(bound);
            }

            ArrayUtils.sort(arr);

            for (int i = 0; i < 100; i++) {
                int a = random.nextInt(bound);
                int b = random.nextInt(bound - a) + a;
                Range range = new Range(a, b);

                assertEquals(goldenSearch(arr, a), BinarySearch.search(arr, a));
                assertEquals(goldenSearch(arr, b), BinarySearch.search(arr, b));
                assertEquals(goldenSearch(arr, range), BinarySearch.search(arr, range));
                assertEquals(BigInteger.valueOf(goldenSearch(arr, a).a), BinarySearch.searchInRange(new Pair(BigInteger.ZERO, BigInteger.valueOf(size)), c -> arr[c.intValue()] >= a));
            }
        }
    }


    @Test
    public void variousBinarySearchTests() {
        assertEquals(BinarySearch.searchInRange(new Range(0, 1000), a -> false), 1000);
        assertEquals(BinarySearch.searchInRange(new Range(0, 1000), a -> true), 0);
        assertEquals(BinarySearch.searchInRange(new Range(0, 1000), a -> a >= 5), 5);
    }



    public Range goldenSearch(int[] array, Range searchForRange) {
        Range range = new Range(array.length, array.length);

        for (int i = 0; i < array.length; i++) {
            if (searchForRange.a <= array[i]) {
                range.a = i;
                break;
            }
        }

        for (int i = range.a; i < array.length; i++) {
            if (searchForRange.b <= array[i]) {
                range.b = i;
                break;
            }
        }

        return range;
    }

    public Range goldenSearch(int[] array, int searchFor) {
        return goldenSearch(array, new Range(searchFor, searchFor + 1));
    }
}
