package lib.vectorization;

import lib.generated.DoubleArrayList;
import lib.generated.IntArrayList;
import lib.utils.MathUtils;

import java.io.Serializable;
import java.util.*;

public class SparseVector implements Serializable {
    private static final int useHashMapAt = 64;

    private IntArrayList inds;
    private DoubleArrayList values;
    private Map<Integer, Double> map;
    int length;
    private double sparseValue;

    public SparseVector(int length, double sparseValue) {
        this.sparseValue = sparseValue;
        this.length = length;
        clear();
    }

    public void clear() {
        inds = new IntArrayList(4);
        values = new DoubleArrayList(4);
        map = null;
    }

    private void switchToMap() {
        map = new HashMap<>(2 * useHashMapAt);
        for (int i = 0; i < inds.size(); i++) {
            map.put(inds.get(i), values.get(i));
        }
        inds = null;
        values = null;
    }

    public int size() {
        return length;
    }

    public double get(int index) {
        rangeCheck(index);

        //if (!couldBeStored(index)) return getSparseValue();


        if (map == null) {
            for (int i = 0; i < inds.size(); i++) {
                if (inds.get(i) == index) return values.get(i);
            }
        } else {
            Double d = map.get(index);
            if (d != null) return d;
        }
        return getSparseValue();
    }

    public double set(int index, double value) {
        rangeCheck(index);

        boolean isSparse = isSparseValue(value);

        if (map == null) {
            int size = inds.size();

            for (int i = 0; i < size; i++) {
                if (inds.get(i) == index) {
                    if (!isSparse) {
                        return values.set(i, value);
                    } else {
                        inds.set(i, inds.get(size - 1));
                        double res = values.set(i, values.get(size - 1));
                        values.remove(size - 1);
                        inds.remove(size - 1);
                        return res;
                    }
                }
            }

            if (isSparse) return value;

            if (size + 1 >= useHashMapAt) switchToMap();
            if (map == null) {
                inds.add(index);
                values.add(value);
                return getSparseValue();
            }
        }

        Double d = isSparse ? map.remove(index) : map.put(index, value);
        if (d == null) {
            return getSparseValue();
        } else {
            return d;
        }
    }


    public void add(int index, double value) {
        rangeCheckAdd(index);

        shift(index, 1);
        set(index, value);
    }

    public double remove(int index) {
        rangeCheck(index);

        double res = get(index);
        shift(index, -1);
        return res;
    }


    private void shift(int index, int by) {
        if (map == null) {
            int size = inds.size();

            for (int i = 0; i < size; i++) {
                int g = inds.get(i);
                if (g >= index) {
                    inds.set(i, g + by);
                }
            }
        } else {
            Map<Integer, Double> n = new HashMap<>(map.size());
            Iterator<Map.Entry<Integer, Double>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Double> entry = iterator.next();
                if (entry.getKey() >= index) {
                    iterator.remove();
                    n.put(entry.getKey() + by, entry.getValue());
                }
            }
            map.putAll(n);
        }

        length += by;
    }





    public double getSparseValue() {
        return sparseValue;
    }

    public boolean isSparseValue(double d) {
        return MathUtils.doubleEquals(d, getSparseValue());
    }






    private void rangeCheck(int index) {
        if (index < 0 || index >= length) throw new IllegalArgumentException("Index out of range: " + index);
    }

    private void rangeCheckAdd(int index) {
        if (index < 0 || index > length) throw new IllegalArgumentException("Index out of range: " + index);
    }



    public VectorElementIterator iterator() {
        if (map == null) {
            return new VectorElementIterator() {
                int cur = 0;

                @Override
                public double getValue() {
                    return values.get(cur - 1);
                }

                @Override
                public double setValue(double value) {
                    return values.set(cur - 1, value);
                }

                @Override
                public boolean hasNext() {
                    return cur < inds.size();
                }

                @Override
                public int next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return inds.get(cur++);
                }
            };
        } else {
            return new VectorElementIterator() {
                Map.Entry<Integer, Double> cur = null;
                Iterator<Map.Entry<Integer, Double>> iterator = SparseVector.this.map.entrySet().iterator();

                @Override
                public double getValue() {
                    return cur.getValue();
                }

                @Override
                public double setValue(double value) {
                    return cur.setValue(value);
                }

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public int next() {
                    return (cur = iterator.next()).getKey();
                }
            };
        }
    }








    public static SparseVector empty(int length, double sparseValue) {
        return new SparseVector(length, sparseValue) {
            @Override
            public double get(int index) {
                return getSparseValue();
            }

            @Override
            public void add(int index, double value) {
                this.length++;
            }

            @Override
            public double remove(int index) {
                this.length--;
                return getSparseValue();
            }

            @Override
            public double set(int index, double value) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
