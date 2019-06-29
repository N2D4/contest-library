import lib.collections.PartitionList;
import lib.utils.various.Range;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PartitionListTests {

    @Test
    public void simpleTests() {
        PartitionList<Integer> list = new PartitionList<>(5000);
        assertEquals(list.get(1337), null);

        list.setRange(new Range(0, 5000), 123);
        list.set(449, 69);
        list.resize(450);
        list.addAll(110, 30, 5);
        list.setAndResize(new Range(100, 130), 80);
        list.removeRange(new Range(125, 135));
        list.addRange(new Range(130, 140), 7);
        list.addAll(0, 20, -10);
        list.applyRange(new Range(18, 22), a -> a * 5);

        PartitionList.PartitionIterator<Integer> itr = list.partitionsIterator(new Range(147, 163));
        itr.next();
        itr.next();
        itr.set(17);


        assertEquals((int) list.get(0), -10);
        assertEquals((int) list.get(17), -10);
        assertEquals((int) list.get(18), -50);
        assertEquals((int) list.get(19), -50);
        assertEquals((int) list.get(20), 615);
        assertEquals((int) list.get(21), 615);
        assertEquals((int) list.get(22), 123);
        assertEquals((int) list.get(99), 123);
        assertEquals((int) list.get(120), 80);
        assertEquals((int) list.get(144), 80);
        assertEquals((int) list.get(145), 5);
        assertEquals((int) list.get(149), 5);
        assertEquals((int) list.get(150), 17);
        assertEquals((int) list.get(159), 17);
        assertEquals((int) list.get(160), 123);
        assertEquals((int) list.get(499), 69);


        Random seedGen = new Random("seed generator for partition lists tests".hashCode());
        List<Long> seeds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            seeds.add(seedGen.nextLong() * seedGen.nextLong());
        }
        for (long seed : seeds) {
            Random random = new Random(seed);

            int index = random.nextInt(list.size());
            ListIterator<Integer> iterator = list.listIterator(index);
            for (int i = 0; i < 1000 * TestConstants.SCALE; i++) {
                if (random.nextBoolean()) {
                    if (iterator.hasNext()) {
                        assertEquals(index, iterator.nextIndex());
                        int actual = list.get(index++);
                        int expected = iterator.next();
                        assertEquals(actual, expected);
                        assertEquals(index, iterator.nextIndex());
                    }
                } else {
                     if (iterator.hasPrevious()) {
                        assertEquals(index - 1, iterator.previousIndex());
                        int actual = list.get(--index);
                        int expected = iterator.previous();
                        assertEquals(actual, expected);
                        assertEquals(index - 1, iterator.previousIndex());
                    }
                }
            }
        }
    }
}
