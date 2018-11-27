import lib.algorithms.BinarySearch;
import lib.graphs.DirectedAdjacencyListGraph;
import lib.graphs.DirectedAdjacencyMatrixGraph;
import lib.graphs.DirectedGraph;
import lib.graphs.algorithms.GraphSearch;
import lib.utils.ArrayUtils;
import lib.utils.various.Range;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinarySearchTests {

    @Test
    public void intArrayBinarySearch() {
        Random random = new Random("int array binary search test".hashCode());
        for (int it = 0; it < 100  * TestConstants.SCALE; it++) {
            int size = random.nextInt(it <= 25 ? it + 1 : 1_000);
            int bound = random.nextInt(random.nextBoolean() ? size + 1 : 2_000_000_000) + 1;

            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(bound);
            }

            Arrays.sort(arr);

            for (int i = 0; i < 100; i++) {
                int a = random.nextInt(bound);
                int b = random.nextInt(bound - a) + a;
                Range range = new Range(a, b);

                assertEquals(goldenSearch(arr, a), BinarySearch.search(arr, a));
                assertEquals(goldenSearch(arr, b), BinarySearch.search(arr, b));
                assertEquals(goldenSearch(arr, range), BinarySearch.search(arr, range));
            }
        }
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
