package lib.utils.various;

import lib.utils.ArrayUtils;
import lib.utils.tuples.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class Sorting {


    public static Pair<Integer, Integer>[] getSortedIndices(int[] arr) {
        return getSortedIndices(ArrayUtils.asList(arr));
    }



    public static <T extends Comparable> Pair<Integer, T>[] getSortedIndices(List<T> list) {
        return getSortedIndices((List<T>) list, null);
    }

    public static <T> Pair<Integer, T>[] getSortedIndices(List<T> list, final Comparator<? super T> comp) {
        Pair<Integer, T>[] result = new Pair[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Pair<>(i, list.get(i));
        }

        if (comp != null) {
            ArrayUtils.sort(result, (a, b) -> comp.compare(a.getValue(), b.getValue()));
        } else {
            ArrayUtils.sort(result, (a, b) -> ((Comparable<T>)a.getValue()).compareTo(b.getValue()));
        }

        return result;
    }

}
