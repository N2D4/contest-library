import lib.collections.CollectTree;
import lib.collections.Multiset;
import lib.collections.TreeMultiset;
import lib.utils.MathUtils;
import lib.utils.tuples.Triple;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectTreeTests {

    @Test
    public void testCollectTree() {
        Random random = new Random("collect tree test".hashCode());

        List<Triple<Supplier<?>, Supplier<?>, BinaryOperator<?>>> tests = new ArrayList<>();
        tests.add(new Triple<>(() -> new TreeMultiset<>(Arrays.asList(random.nextInt(4), random.nextInt(26))), (Supplier<?>) TreeMultiset::new, (Multiset a, Multiset b) -> {
            a.addAll(b);
            return a;
        }));
        tests.add(new Triple<>(random::nextInt, () -> 0, (BinaryOperator<Integer>) Integer::sum));
        tests.add(new Triple<>(random::nextInt, () -> 1, (BinaryOperator<Integer>) (a, b) -> a * b));

        for (int i = 0; i < 100 * TestConstants.SCALE; i++) {
            for (Triple<Supplier<?>, Supplier<?>, BinaryOperator<?>> test : tests) {
                int size = MathUtils.sq(1 + Math.min(random.nextInt(25), i));
                Object[] arr = new Object[size];
                for (int j = 0; j < arr.length; j++) {
                    arr[j] = test.a.get();
                }
                int arity = MathUtils.sq(random.nextInt(7) + 1) + 1 + random.nextInt(3);

                CollectTree<?> tree = new CollectTree<Object>((Supplier<Object>) test.b, (BinaryOperator<Object>) test.c, Arrays.asList(arr), arity);

                for (int j = 0; j < size + 3; j++) {
                    Object golden = test.b.get();
                    int start = random.nextInt(tree.size());
                    int end = random.nextInt(tree.size() - start + 1) + start;
                    for (int k = start; k < end; k++) {
                        golden = ((BinaryOperator<Object>) test.c).apply(golden, arr[k]);
                    }
                    Object obj = tree.collect(start, end);
                    assertEquals(obj, golden);
                }
            }
        }
    }

}
