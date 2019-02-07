package lib.vectorization;

import lib.utils.MathUtils;
import lib.utils.Utils;
import lib.utils.tuples.Triple;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractMatrix implements Matrix, Serializable {


    @Override
    public String toString() {
        final int cellLength = 3;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getRowCount(); i++) {
            if (i != 0) result.append("\n");
            for (int j = 0; j < getColumnCount(); j++) {
                if (j != 0) result.append(", ");
                result.append(this.get(i, j));
            }
        }

        return result.toString();
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Matrix)) return false;

        Matrix other = (Matrix) obj;
        if (this.getColumnCount() != other.getColumnCount()) return false;
        if (this.getRowCount() != other.getRowCount()) return false;

        Matrix o1 = this;
        Matrix o2 = other;
        if (o2 instanceof SparseMatrix) {
            Matrix o3 = o1;
            o1 = o2;
            o2 = o3;
        }

        for (int it = 0; it < 2; it++) {
            if (o1 instanceof DenseMatrix) {
                for (int i = 0; i < o1.getRowCount(); i++) {
                    for (int j = 0; j < o1.getColumnCount(); j++) {
                        if (o1.get(i, j) != o2.get(i, j)) return false;
                    }
                }
                return true;
            }

            for (Triple<Integer, Integer, Double> next : o1) {
                if (o2.get(next.a, next.b) != next.c) return false;
            }

            Matrix o3 = o1;
            o1 = o2;
            o2 = o3;
        }


        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Utils.hashAll(this.iterator()), this.getRowCount(), this.getColumnCount());

        /*
        final int hashCountMax = 10000;

        if (this.getElementCount() <= hashCountMax)
            return Objects.hash(Utils.hashAll(this.iterator()), this.getRowCount(), this.getColumnCount());


        int hash = Objects.hash(this.getRowCount(), this.getColumnCount());
        for (int i = 0; i < hashCountMax; i++) {
            int pos = (hash + i) % this.getElementCount();
            hash = Objects.hash(
                    hash,
                    this.getElementCount(),
                    this.get(pos % this.getRowCount(), pos / this.getRowCount())
            );
        }
        return hash;*/
    }








    protected static void copyAllNon(Matrix from, Matrix to, double value) {
        if (from.getRowCount() > to.getRowCount() || from.getColumnCount() > to.getColumnCount()) throw new IllegalArgumentException("from matrix too large");
        if (from instanceof SparseMatrix && MathUtils.doubleEquals(((SparseMatrix) from).getSparseValue(), value)) {
            for (Triple<Integer, Integer, Double> t : from) {
                to.set(t.getLeft(), t.getMiddle(), t.getRight());
            }
        } else {
            for (int i = 0; i < from.getRowCount(); i++) {
                for (int j = 0; j < from.getColumnCount(); j++) {
                    double g = from.get(i, j);
                    if (!MathUtils.doubleEquals(value, g)) to.set(i, j, g);
                }
            }
        }
    }




    protected void rangeChecks(int row, int column) {
        if (row < 0 || column < 0 || row >= getRowCount() || column >= getColumnCount()) throw new IllegalArgumentException();
    }








    /* BEGIN-POLYFILL-6 *../
    @Override public int getElementCount() {
        return getRowCount() * getColumnCount();
    }

    @Override
    public boolean isSquare() {
        return this.getColumnCount() == this.getRowCount();
    }


    @Override
    public NavigableMap<Integer, Double> getRowNonZeroes(int row) {
        return getRowNonValued(row, 0);
    }
    @Override
    public NavigableMap<Integer, Double> getRowNonValued(int row, double value) {
        if (row < 0 || row >= getRowCount()) throw new IllegalArgumentException();

        TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
        for (int i = 0; i < getColumnCount(); i++) {
            double d = get(row, i);
            if (!MathUtils.doubleEquals(value, d)) map.put(i, d);
        }
        return map;
    }
    @Override
    public NavigableMap<Integer, Double> getColumnNonZeroes(int column) {
        return getColumnNonValued(column, 0);
    }
    @Override
    public NavigableMap<Integer, Double> getColumnNonValued(int column, double value) {
        if (column < 0 || column >= getColumnCount()) throw new IllegalArgumentException();

        TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
        for (int i = 0; i < getRowCount(); i++) {
            double d = get(i, column);
            if (!MathUtils.doubleEquals(value, d)) map.put(i, d);
        }
        return map;
    }


    @Override
    public Iterator<Triple<Integer, Integer, Double>> iterator() {
        return new PolyfillIterator<Triple<Integer, Integer, Double>>() {
            private int i = 0;
            private double result;

            @Override
            public boolean hasNext() {
                while (i < getElementCount()) {
                    double r = get(i % getRowCount(), i / getRowCount());
                    if (r != 0) {
                        result = r;
                        return true;
                    }
                    i++;
                }
                return false;
            }

            @Override
            public Triple<Integer, Integer, Double> next() {
                if (!hasNext()) throw new NoSuchElementException();
                Triple<Integer, Integer, Double> t = new Triple(i % getRowCount(), i / getRowCount(), result);
                i++;
                return t;
            }
        };
    }

    @Override
    public void setAll(double value) {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                set(i, j, value);
            }
        }
    }
    /..* END-POLYFILL-6 */

}
