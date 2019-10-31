package lib.vectorization;

import lib.utils.MathUtils;
import lib.utils.Utils;
import lib.utils.tuples.Triple;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractMatrix implements Matrix, Serializable {


    @Override
    public String toString() {
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
    }


    /**
     * Copies all elements that are not double-equal to value.
     */
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

}
