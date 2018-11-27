package lib.utils;

import lib.utils.various.Range;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ArrayUtils {

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
        public int size() {
            return length;
        }
    }




}
