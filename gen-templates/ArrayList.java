package lib.generated;

import lib.utils.various.Range;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class $CAP_TYPE$ArrayList implements Serializable {
    private $TYPE$[] arr;
    private int length = 0;

    public $CAP_TYPE$ArrayList() {
        this(10);
    }

    public $CAP_TYPE$ArrayList(int initialCapacity) {
        this.arr = new $TYPE$[initialCapacity];
    }

    public $CAP_TYPE$ArrayList($TYPE$[] array) {
        this.arr = Arrays.copyOf(array, array.length);
    }

    public $CAP_TYPE$ArrayList(List<$WRAPPER_TYPE$> list) {
        this.arr = new $TYPE$[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
    }

    public static $CAP_TYPE$ArrayList ofSize(int size) {
        $CAP_TYPE$ArrayList result = new $CAP_TYPE$ArrayList(size);
        result.length = size;
        return result;
    }

    public static $CAP_TYPE$ArrayList ofSize(int size, $TYPE$ element) {
        $CAP_TYPE$ArrayList result = new $CAP_TYPE$ArrayList(size);
        result.length = size;
        for (int i = 0; i < element; i++) {
            result.arr[i] = element;
        }
        return result;
    }




    public $TYPE$[] toArray() {
        return Arrays.copyOf(arr, length);
    }





    public int size() {
        return length;
    }

    public $TYPE$ get(int index) {
        rangeCheck(index);
        return arr[index];
    }

    public $TYPE$ set(int index, $TYPE$ val) {
        rangeCheck(index);
        $TYPE$ prev = arr[index];
        arr[index] = val;
        return prev;
    }

    public void add($TYPE$ val) {
        ensureCapacity(length + 1);
        arr[length] = val;
        length++;
    }

    public void add(int index, $TYPE$ val) {
        rangeCheckAdd(index);
        shift(index, 1);
        arr[index] = val;
    }

    public void addAll($TYPE$[] val) {
        addAll(length, val);
    }

    public void addAll(int index, $TYPE$[] val) {
        rangeCheckAdd(index);
        shift(index, val.length);
        for (int i = 0; i < val.length; i++) {
            arr[index + i] = val[i];
        }
    }

    public $TYPE$ remove(int index) {
        rangeCheck(index);
        $TYPE$ prev = arr[index];
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
