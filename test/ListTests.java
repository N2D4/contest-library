import lib.collections.OmniList;
import lib.utils.Utils;
import lib.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListTests {
    @Test
    public void listOperationsTest() {
        OmniList<Integer> list = new OmniList<>();
        ArrayList<Integer> golden = new ArrayList<>();

        final DOER<List<Integer>> D = new DOER<List<Integer>>() {
            @Override
            public void check() {
                assertTrue(list.isConsistent());
                assertEquals(list, golden);
            }

            @Override
            public Pair<List<Integer>, List<Integer>> getBoth() {
                return new Pair<>(list, golden);
            }
        };


        D.check();

        D.bothAndCheckF(l -> l.add(5));
        D.bothAndCheckF(l -> l.add(7));
        D.bothAndCheckC(l -> l.add(1, 5));
        D.bothAndCheckC(l -> l.add(1, 5));
        D.bothAndCheckF(l -> l.set(1, 3));
        D.bothAndCheckF(l -> l.remove((Object) 5));
        D.bothAndCheckF(l -> l.remove((Object) 5));
        D.bothAndCheckC(l -> l.add(0, 5));
        D.bothAndCheckC(l -> l.add(0, 5));
        D.bothAndCheckC(l -> l.remove(3));

        D.bothAndCheckC(l -> l.indexOf(3));
        D.bothAndCheckC(l -> l.indexOf(5));
        D.bothAndCheckC(l -> l.indexOf(7));
        D.bothAndCheckC(l -> l.indexOf(999999994));
        D.bothAndCheckC(l -> l.lastIndexOf(3));
        D.bothAndCheckC(l -> l.lastIndexOf(5));
        D.bothAndCheckC(l -> l.lastIndexOf(7));
        D.bothAndCheckC(l -> l.lastIndexOf(999999994));
        D.bothAndCheckC(l -> l.contains(3));
        D.bothAndCheckC(l -> l.contains(5));
        D.bothAndCheckC(l -> l.contains(7));
        D.bothAndCheckC(l -> l.contains(999999994));
        D.bothAndCheckC(l -> l.remove((Object) 3));
        D.bothAndCheckC(l -> l.remove((Object) 3));


    }




    private interface DOER<T> {
        void check();
        Pair<T, T> getBoth();
        default boolean bothF(Function<T, ?> func) {
            Pair<T, T> pair = getBoth();
            return Utils.equals(func.apply(pair.a), func.apply(pair.b));
        }
        default void bothAndCheckF(Function<T, ?> func) {
            assertTrue(bothF(func));
            check();
        }
        default void bothC(Consumer<T> cons) {
            Pair<T, T> pair = getBoth();
            cons.accept(pair.a);
            cons.accept(pair.b);
        }
        default void bothAndCheckC(Consumer<T> cons) {
            bothC(cons);
            check();
        }
    }
}
