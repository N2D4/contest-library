import lib.utils.Utils;
import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* GENERIFY-THIS */
public class ArrayList<T> extends AbstractList<T> {
    private /*BOX T*/Object[] arr;
    private int length = 0;

    public ArrayList() {
        this(10);
    }

    public ArrayList(int initialCapacity) {
        this.arr = new /*BOX T*/Object[initialCapacity];
    }

    public ArrayList(T[] array) {
        this.arr = Arrays.copyOf(array, array.length);
        this.length = array.length;
    }

    public ArrayList(Iterable<T> other) {
        this();
        for (Iterator<T> iter = other.iterator(); iter.hasNext();) {
            this.add(iter.next());
        }
    }



    @Override
    public /*BOX T*/Object[] toArray() {
        return Arrays.copyOf(arr, length);
    }





    @Override
    public int size() {
        return length;
    }

    @Override
    public T get(int index) {
        //rangeCheck(index);
        return (T) arr[index];
    }

    @Override
    public T set(int index, T val) {
        //rangeCheck(index);
        T prev = (T) arr[index];
        arr[index] = val;
        return prev;
    }

    @Override
    public void add(T val) {
        ensureCapacity(length + 1);
        arr[length] = val;
        length++;
    }

    @Override
    public void add(int index, T val) {
        //rangeCheckAdd(index);
        shift(index, 1);
        arr[index] = val;
    }

    @Override
    public void addAll(T[] val) {
        addAll(length, val);
    }

    @Override
    public void addAll(int index, T[] val) {
        rangeCheckAdd(index);
        shift(index, val.length);
        System.arraycopy(val, 0, this.arr, index, val.length);
    }

    @Override
    public T remove(int index) {
        rangeCheck(index);
        T prev = (T) arr[index];
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
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int indx = 0;
            @Override
            public boolean hasNext() {
                return indx < length;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (T) arr[indx++];
            }

            @Override
            public void remove() {
                if (indx <= 0) throw new NoSuchElementException();
                /*PREFIX T*/ArrayList.this.remove(--indx);
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
