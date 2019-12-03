/* Generated by Generificator on Tue Dec 03 15:26:20 CET 2019 from ArrayList.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.utils.Utils;
import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class IntArrayList extends IntAbstractList {
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
        this.length = array.length;
    }

    public IntArrayList(IntIterable other) {
        this();
        for (IntIterator iter = other.iterator(); iter.hasNext();) {
            this.add(iter.next());
        }
    }



    @Override
    public int[] toArray() {
        return Arrays.copyOf(arr, length);
    }





    @Override
    public int size() {
        return length;
    }

    @Override
    public int get(int index) {
        //rangeCheck(index);
        return (int) arr[index];
    }

    @Override
    public int set(int index, int val) {
        //rangeCheck(index);
        int prev = (int) arr[index];
        arr[index] = val;
        return prev;
    }

    @Override
    public void add(int val) {
        ensureCapacity(length + 1);
        arr[length] = val;
        length++;
    }

    @Override
    public void add(int index, int val) {
        //rangeCheckAdd(index);
        shift(index, 1);
        arr[index] = val;
    }

    @Override
    public void addAll(int[] val) {
        addAll(length, val);
    }

    @Override
    public void addAll(int index, int[] val) {
        rangeCheckAdd(index);
        shift(index, val.length);
        System.arraycopy(val, 0, this.arr, index, val.length);
    }

    @Override
    public int remove(int index) {
        rangeCheck(index);
        int prev = (int) arr[index];
        shift(index + 1, -1);
        return prev;
    }

    @Override
    public void removeAll(Range range) {
        rangeCheck(range.a);
        rangeCheck(range.b);
        range.ensureValidity();
        shift(range.b, range.a - range.b);
    }

    @Override
    public void clear() {
        this.length = 0;
    }

    @Override
    public IntIterator iterator() {
        return new IntIterator() {
            int indx = 0;
            @Override
            public boolean hasNext() {
                return indx < length;
            }

            @Override
            public int next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (int) arr[indx++];
            }
        };
    }

    @Override
    public void reverse() {
        int ln = size() / 2;
        int la = length - 1;
        for (int i = 0; i < ln; i++) {
            swap(i, la - i);
        }
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
