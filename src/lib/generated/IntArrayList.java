package lib.generated;

import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class IntArrayList implements Serializable {
    private int[] arr;
    private int length = 0;

    public IntArrayList() {
        this(10);
    }

    public IntArrayList(int initialCapacity) {
        this.arr = new int[initialCapacity];
    }

    public IntArrayList(int[] array) {
        this.arr = Arrays.copyOf(array, array.length);
    }

    public IntArrayList(List<Integer> list) {
        this.arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
    }

    public static IntArrayList ofSize(int size) {
        IntArrayList result = new IntArrayList(size);
        result.length = size;
        return result;
    }

    public static IntArrayList ofSize(int size, int element) {
        IntArrayList result = new IntArrayList(size);
        result.length = size;
        for (int i = 0; i < element; i++) {
            result.arr[i] = element;
        }
        return result;
    }




    public int[] toArray() {
        return Arrays.copyOf(arr, length);
    }





    public int size() {
        return length;
    }

    public int get(int index) {
        return arr[index];
    }

    public int set(int index, int val) {
        int prev = arr[index];
        arr[index] = val;
        return prev;
    }

    public void add(int val) {
        ensureCapacity(length + 1);
        arr[length] = val;
        length++;
    }

    public void add(int index, int val) {
        rangeCheckAdd(index);
        shift(index, 1);
        arr[index] = val;
    }

    public void addAll(int[] val) {
        addAll(length, val);
    }

    public void addAll(int index, int[] val) {
        rangeCheckAdd(index);
        shift(index, val.length);
        for (int i = 0; i < val.length; i++) {
            arr[index + i] = val[i];
        }
    }

    public int remove(int index) {
        int prev = arr[index];
        shift(index + 1, -1);
        return prev;
    }

    public void removeAll(Range range) {
        rangeCheck(range.a);
        rangeCheck(range.b);
        range.ensureValidity();
        shift(range.b, range.a - range.b);
    }

    public void clear() {
        shift(length, -length);
    }






    private void shift(int startIndex, int shift) {
        ensureCapacity(length + shift);
        if (shift > 0) {
            for (int i = length - 1; i >= startIndex; i--) {
                arr[i + shift] = arr[i];
            }
        } else if (shift < 0) {
            for (int i = startIndex; i < length; i++) {
                arr[i + shift] = arr[i];
            }
        }
        length += shift;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= length) throw new IllegalArgumentException("Index out of range: " + index);
    }

    private void rangeCheckAdd(int index) {
        if (index < 0 || index > length) throw new IllegalArgumentException("Index out of range: " + index);
    }


    private void ensureCapacity(int minCapacity) {
        if (arr.length >= minCapacity) return;

        int newCapacity = minCapacity + length;
        if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;
        arr = Arrays.copyOf(arr, newCapacity);
    }
}
