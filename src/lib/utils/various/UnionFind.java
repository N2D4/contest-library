package lib.utils.various;

import lib.utils.ArrayUtils;
import lib.utils.Utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class UnionFind {
    private final int[] arr;

    public UnionFind(int size) {
        this.arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
    }

    public int size() {
        return arr.length;
    }

    public int find(int i) {
        int original = i;
        while (arr[i] != i) {
            i = arr[i];
        }
        while (i != original) {
            int no = arr[original];
            arr[original] = i;
            original = no;
        }
        return i;
    }

    public void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (Math.random() < 0.5) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        arr[b] = a;
    }

    public void compressAll() {
        for (int i = 0; i < size(); i++) {
            find(i);
        }
    }

    public Collection<Set<Integer>> getComponents() {
        compressAll();
        return Collections.unmodifiableCollection(Utils.invert(ArrayUtils.asList(arr)).values());
    }

    @Override
    public String toString() {
        return getComponents().toString();
    }
}
