package lib.utils;

import lib.generated.DoubleExtendedStream;
import lib.generated.IntExtendedStream;
import lib.generated.LongExtendedStream;
import lib.utils.various.ExtendedStream;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayUtils {

    //endregion

    //region stream()
    public static IntExtendedStream stream(int... arr) {
        return Utils.stream(arr);
    }

    public static LongExtendedStream stream(long... arr) {
        return Utils.stream(arr);
    }

    public static DoubleExtendedStream stream(double... arr) {
        return Utils.stream(arr);
    }

    public static <T> ExtendedStream<T> stream(T... arr) {
        return Utils.stream(arr);
    }

    //endregion

    //region sort()
    /**
     * Like Arrays.sort, but shuffles the elements first, making it less vulnerable to attacks. Both best and worst case
     * complexity are now O(n log n) (instead of O(n) to O(n^2)).
     */
    public static void sort(int[] arr) {
        shuffle(arr);
        Arrays.sort(arr);
    }

    /**
     * Like Arrays.sort, but shuffles the elements first, making it less vulnerable to attacks. Both best and worst case
     * complexity are now O(n log n) (instead of O(n) to O(n^2)).
     */
    public static void sort(long[] arr) {
        shuffle(arr);
        Arrays.sort(arr);
    }

    /**
     * Like Arrays.sort, but shuffles the elements first, making it less vulnerable to attacks. Both best and worst case
     * complexity are now O(n log n) (instead of O(n) to O(n^2)).
     */
    public static void sort(double[] arr) {
        shuffle(arr);
        Arrays.sort(arr);
    }

    /**
     * Like Arrays.sort. Does not shuffle the elements first, yet is still not vulnerable to attacks as it uses Timsort
     * (and is therefore stable).
     */
    public static <T> void sort(T[] arr) {
        Arrays.sort(arr);
    }

    /**
     * Like Arrays.sort. Does not shuffle the elements first, yet is still not vulnerable to attacks as it uses Timsort
     * (and is therefore stable).
     */
    public static <T> void sort(T[] arr, Comparator<? super T> comparator) {
        Arrays.sort(arr, comparator);
    }
    //endregion

    //region shuffle()
    public static void shuffle(int[] arr) {
        shuffle(arr, ThreadLocalRandom.current());
    }

    public static void shuffle(long[] arr) {
        shuffle(arr, ThreadLocalRandom.current());
    }

    public static void shuffle(double[] arr) {
        shuffle(arr, ThreadLocalRandom.current());
    }

    public static void shuffle(int[] arr, Random random) {
        for (int i = 0; i < arr.length - 1; i++) {
            swap(arr, i, i + random.nextInt(arr.length - i));
        }
    }

    public static void shuffle(long[] arr, Random random) {
        for (int i = 0; i < arr.length - 1; i++) {
            swap(arr, i, i + random.nextInt(arr.length - i));
        }
    }

    public static void shuffle(double[] arr, Random random) {
        for (int i = 0; i < arr.length - 1; i++) {
            swap(arr, i, i + random.nextInt(arr.length - i));
        }
    }

    //endregion

    //region swap()
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(long[] arr, int i, int j) {
        long tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(double[] arr, int i, int j) {
        double tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(boolean[] arr, int i, int j) {
        boolean tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static <T> void swap(T[] arr, int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    //endregion

    //endregion

    public static long[] toLongArray(int[] arr) {
        return Arr.stream(arr).mapToLong(a -> a).toArray();
    }

    //endregion()

    //region asList()
    public static List<Byte> asList(byte... arr) {
        return new BackedList<>(arr);
    }

    public static List<Character> asList(char... arr) {
        return new BackedList<>(arr);
    }

    public static List<Short> asList(short... arr) {
        return new BackedList<>(arr);
    }

    public static List<Integer> asList(int... arr) {
        return new BackedList<>(arr);
    }

    public static List<Double> asList(double... arr) {
        return new BackedList<>(arr);
    }

    public static List<Long> asList(long... arr) {
        return new BackedList<>(arr);
    }

    public static <T> List<T> asList(T... arr) {
        return Arrays.asList(arr);
    }


    private static class BackedList<T, A> extends AbstractList<T> implements RandomAccess {
        private final A arr;
        private final int length;
        public BackedList(A arr) {
            this.arr = arr;
            this.length = java.lang.reflect.Array.getLength(arr);
        }

        @Override
        public T get(int index) {
            return (T) java.lang.reflect.Array.get(arr, index);
        }

        @Override
        public T set(int index, T val) {
            T oldValue = get(index);
            java.lang.reflect.Array.set(arr, index, val);
            return oldValue;
        }

        @Override
        public int size() {
            return length;
        }
    }
    //endregion

    //region iterator()
    public static <T> Iterator<T> iterator(T... arr) {
        return asList(arr).iterator();
    }
    //endregion

    //region verboseCopy()

    /**
     * Copies the elements in src in the range [srcFromIndex, srcToIndex) to dest at destFromIndex. If
     * srcToIndex < srcFromIndex, the copying will wrap around.
     *
     * In other words, the function does something like this (in py-, uh, pseudo-code):
     *
     *   if sfi <= sti:
     *       dst[dfi:dfi+(sti-sfi)] = src[sfi:sti]
     *   else:
     *       dst[dfi:dfi+(sti-sfi)] = src[sfi:] + src[:sti]
     *
     */
    public static <T> int verboseCopy(T[] src, int srcFromIndex, int srcToIndex, T[] dest, int destFromIndex) {
        int length = srcToIndex - srcFromIndex;
        if (length < 0) {
            length += src.length;
            int copied = verboseCopy(src, srcFromIndex, src.length, dest, destFromIndex);
            return copied + verboseCopy(src, 0, srcToIndex, dest, destFromIndex + copied);
        } else {
            System.arraycopy(src, srcFromIndex, dest, destFromIndex, length);
            return length;
        }
    }
    //endregion

}
