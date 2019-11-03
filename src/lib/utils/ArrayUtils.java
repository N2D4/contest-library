package lib.utils;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
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





    public static List<Byte> asSortedSet(byte[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Character> asSortedSet(char[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Short> asSortedSet(short[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Integer> asSortedSet(int[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Boolean> asSortedSet(boolean[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Float> asSortedSet(float[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Double> asSortedSet(double[] arr) {
        return new BackedList<>(arr);
    }

    public static List<Long> asSortedSet(long[] arr) {
        return new BackedList<>(arr);
    }

    public static <T> List<T> asSortedSet(T[] arr) {
        return Arrays.asList(arr);
    }



}
