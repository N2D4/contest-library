import lib.collections.HashMultiset;
import lib.generated.IntArrayList;
import lib.utils.Arr;
import lib.utils.Utils;
import lib.utils.tuples.Monad;
import lib.utils.tuples.Pair;
import lib.utils.various.ExtendedStream;
import lib.utils.various.Range;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTests {
    
    @Test
    public void intStreamOldMethodsTests() {
        assertEquals(20, Arr.stream(new double[] {0, 3, 5, 5, 4, 3}).sum());
        assertTrue(Math.abs(3.3333333333 - Arr.stream(new double[] {0, 3, 5, 5, 4, 3}).average().getAsDouble()) < 0.0001);
        assertEquals(15, Arr.stream(new double[] {1, 9, 10, 15, 7, 0, 17}).filter(a -> a > 10).findFirst().getAsDouble());
        assertEquals(15, Arr.stream(new double[] {1, 9, 10, 15, 7, 0, 17}).filter(a -> a > 10).findAny().getAsDouble());
        assertEquals(Arr.asList(new double[] {0, 1, 7, 9, 10, 15, 17}), Arr.stream(new double[] {1, 9, 10, 15, 7, 0, 17}).sorted().boxed().collect(toList()));
    }

    @Test
    public void objectStreamOldMethodsTests() {
        Collector<Object, ?, HashMultiset<Object>> unord = Collectors.toCollection(() -> new HashMultiset<>());

        List<BiFunction<Stream, long[], Object>> tests = new ArrayList<>();
        tests.add(
                (s, l) -> s.collect(unord)
        );
        tests.add(
                (s, l) -> s.sorted(Comparator.comparing(Object::hashCode)).collect(toList())
        );
        tests.add(
                (s, l) -> s.distinct().sorted(Comparator.comparing(Object::hashCode)).collect(toList())
        );
        tests.add(
                (s, l) -> s.sorted(Comparator.comparing(Object::hashCode)).distinct().collect(toList())
        );
        tests.add(
                (s, l) -> s.distinct().collect(unord)
        );
        tests.add(
                (s, l) -> s.distinct().map(a -> a.getClass().hashCode() + a.hashCode()).sorted().findFirst()
        );
        tests.add(
                (s, l) -> s.map(Object::toString).filter(a -> !a.equals("5")).sorted().collect(toList())
        );
        tests.add(
                (s, l) -> s.map(Object::toString).sorted().filter(a -> !a.equals("5")).sorted().collect(toList())
        );
        tests.add(
                (s, l) -> s.filter(a -> !a.equals(5)).map(a -> a.toString()).sorted().collect(toList())
        );
        tests.add(
                (s, l) -> s.filter(a -> !a.equals(5)).map(a -> a.toString()).sorted().findFirst()
        );


        int i = 0;
        for (BiFunction<Stream, long[], Object> cons : tests) {
            for (boolean parallel : new boolean[]{false, true}) {
                int j = 0;
                for (Pair<ExtendedStream, Stream> p : getStreams()) {
                    long[] ai = new long[1], bi = new long[1];
                    if (parallel) p = new Pair<>(p.a.parallel(), (Stream) p.b.parallel());
                    //System.err.println(i + " " + parallel + " " + j++);
                    assertEquals(cons.apply(p.b, bi), cons.apply(p.a, ai));
                }
            }
            i++;
        }
    }


    private List<Pair<ExtendedStream, Stream>> getStreams() {
        List<Pair<ExtendedStream, Stream>> rComps = new ArrayList<>();
        for (boolean parallel : new boolean[] {false, true}) {
            List<Pair<ExtendedStream, Stream>> comps = new ArrayList<>();
            comps.add(new Pair<>(
                    Utils.stream(new Range(0, 1)).boxed(),
                    IntStream.range(0, 1).boxed()
            ));
            comps.add(new Pair<>(
                    Utils.stream(new Range(0, 5)).boxed(),
                    IntStream.range(0, 5).boxed()
            ));
            comps.add(new Pair<>(
                    Utils.stream(new Range(0, 1000)).boxed(),
                    IntStream.range(0, 1000).boxed()
            ));
            Object[][] arrs = new Object[][]{
                    {"hello", "world", "what", "is", "up"},
                    {0, 5, 2, 7, -10000, 5},
                    {42, "okie", 2, new Monad(new IntArrayList()), 1337},
                    {},
                    {2, 3, 5, 7, 11, 13, 17, 19l, 23, 29, 31, 37, 1_000_000_007},
                    {new Pair<>(15, 17), new Pair<>(13, -6000)},
                    {7, 5, 4, 3, 2, 1, 0, -10},
                    {new Monad<>(5), new Monad<>(3), new Monad<>(5), new Monad<>(5), new Monad<>(3), new Monad<>(2), new Monad<>(5), new Monad<>(5), new Monad<>(5), new Monad<>(2), new Monad<>(2), new Monad<>(17), new Monad<>(5), new Monad<>(3), new Monad<>(5)},
            };
            for (Object[] arr : arrs) {
                comps.add(new Pair<>(
                        Arr.stream(arr),
                        Arrays.stream(arr)
                ));
            }
            comps.stream().map(a -> new Pair<>(a.a.setParallel(parallel), a.b)).forEachOrdered(rComps::add);
        }
        return rComps;
    }

}
