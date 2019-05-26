import lib.algorithms.BinarySearch;
import lib.algorithms.Histograms;
import lib.utils.PrimeUtils;
import lib.utils.tuples.Pair;
import lib.utils.various.Range;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistogramTests {

    @Test
    public void maxAreaTest() {
        Random random = new Random("max area tests".hashCode());
        for (long i = 0; i < 1000 * TestConstants.SCALE; i++) {
            int[] arr = new int[random.nextInt((int) Math.min(i + 1, 200))];
            for (int j = 0; j < arr.length; j++) {
                arr[j] = random.nextInt(26);
            }
            int golden = goldenArea(arr);
            Pair<Range, Integer> a = Histograms.maxArea(arr);
            assertEquals((Integer) golden, a.getValue());
            assertEquals(golden, goldenCalc(arr, a.a.a, a.a.b));
        }
    }



    private int goldenArea(int[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i+1; j <= arr.length; j++) {
                max = Math.max(max, goldenCalc(arr, i, j));
            }
        }
        return max;
    }

    private int goldenCalc(int[] arr, int i, int j) {
        int min = Integer.MAX_VALUE;
        for (int k = i; k < j; k++) {
            min = Math.min(arr[k], min);
        }
        return (j-i) * min;
    }
}
