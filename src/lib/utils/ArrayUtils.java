package lib.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ArrayUtils {

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

    public static void shuffle(boolean[] arr) {
        shuffle(arr, ThreadLocalRandom.current());
    }

    public static <T> void shuffle(T[] arr) {
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

    public static <T> void shuffle(T[] arr, Random random) {
        for (int i = 0; i < arr.length - 1; i++) {
            swap(arr, i, i + random.nextInt(arr.length - i));
        }
    }

    public static void shuffle(boolean[] arr, Random random) {
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

    //region indexOf()
    public static int indexOf(int[] arr, int of) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == of) return i;
        }
        return -1;
    }

    public static int indexOf(boolean[] arr, boolean of) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == of) return i;
        }
        return -1;
    }

    public static int indexOf(double[] arr, double of) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == of) return i;
        }
        return -1;
    }

    public static int indexOf(long[] arr, long of) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == of) return i;
        }
        return -1;
    }

    public static int indexOf(Object[] arr, Object of) {
        for (int i = 0; i < arr.length; i++) {
            if (Utils.equals(arr[i], of)) return i;
        }
        return -1;
    }
    //endregion

    //region to[Int|Long|Double]Array()
    public static int[] toIntArray(byte[] arr) {
        return asList(arr).stream().mapToInt(a -> a).toArray();
    }

    public static int[] toIntArray(short[] arr) {
        return asList(arr).stream().mapToInt(a -> a).toArray();
    }

    public static int[] toIntArray(char[] arr) {
        return asList(arr).stream().mapToInt(a -> a).toArray();
    }

    public static long[] toLongArray(byte[] arr) {
        return asList(arr).stream().mapToLong(a -> a).toArray();
    }

    public static long[] toLongArray(short[] arr) {
        return asList(arr).stream().mapToLong(a -> a).toArray();
    }

    public static long[] toLongArray(char[] arr) {
        return asList(arr).stream().mapToLong(a -> a).toArray();
    }

    public static long[] toLongArray(int[] arr) {
        return Arrays.stream(arr).mapToLong(a -> a).toArray();
    }

    public static double[] toDoubleArray(float[] arr) {
        return asList(arr).stream().mapToDouble(a -> a).toArray();
    }
    //endregion()

    //region asList()
    public static List<Byte> asList(byte[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Character> asList(char[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Short> asList(short[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Integer> asList(int[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Boolean> asList(boolean[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Float> asList(float[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Double> asList(double[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Long> asList(long[] arr) {
        return new BackedList<>(arr);
    }

    public static <T> List<T> asList(T[] arr) {
        return Arrays.asList(arr);
    }


    private static class BackedList<T, A> extends AbstractList<T> {
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


    public static <T> Iterator<T> iterator(T... arr) {
        return asList(arr).iterator();
    }
}
