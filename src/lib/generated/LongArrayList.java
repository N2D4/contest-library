/* Generated by Generificator on Fri Nov 08 00:29:31 CET 2019 from ArrayList.java */

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


public class LongArrayList extends LongAbstractList {
    private long[] arr;
    private int length = 0;

    public LongArrayList() {
        this(10);
    }

    public LongArrayList(int initialCapacity) {
        this.arr = new long[initialCapacity];
    }

    public LongArrayList(long[] array) {
        this.arr = Arrays.copyOf(array, array.length);
        this.length = array.length;
    }

    public LongArrayList(LongIterable other) {
        this();
        for (LongIterator iter = other.iterator(); iter.hasNext();) {
            this.add(iter.next());
        }
    }



    @Override
    public long[] toArray() {
        return Arrays.copyOf(arr, length);
    }





    @Override
    public int size() {
        return length;
    }

    @Override
    public long get(int index) {
        //rangeCheck(index);
        return (long) arr[index];
    }

    @Override
    public long set(int index, long val) {
        //rangeCheck(index);
        long prev = (long) arr[index];
        arr[index] = val;
        return prev;
    }

    @Override
    public void add(long val) {
        ensureCapacity(length + 1);
        arr[length] = val;
        length++;
    }

    @Override
    public void add(int index, long val) {
        //rangeCheckAdd(index);
        shift(index, 1);
        arr[index] = val;
    }

    @Override
    public void addAll(long[] val) {
        addAll(length, val);
    }

    @Override
    public void addAll(int index, long[] val) {
        rangeCheckAdd(index);
        shift(index, val.length);
        System.arraycopy(val, 0, this.arr, index, val.length);
    }

    @Override
    public long remove(int index) {
        rangeCheck(index);
        long prev = (long) arr[index];
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
    public LongIterator iterator() {
        return new LongIterator() {
            int indx = 0;
            @Override
            public boolean hasNext() {
                return indx < length;
            }

            @Override
            public long next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (long) arr[indx++];
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
