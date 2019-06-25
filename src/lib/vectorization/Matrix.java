package lib.vectorization;

import lib.utils.MathUtils;
import lib.utils.tuples.Triple;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Matrix extends Iterable<Triple<Integer, Integer, Double>> {
    double get(int row, int column);
    double set(int row, int column, double value);
    default void setAll(double value) {
        apply(a -> value);
    }

    default void apply(Function<Double, Double> func) {
        int n = this.getRowCount();
        int m = this.getColumnCount();
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < m; b++) {
                this.set(a, b, func.apply(this.get(a, b)));
            }
        }
    }

    int getRowCount();
    int getColumnCount();
    /* BEGIN-JAVA-8 */
    default int getElementCount() {
        return getRowCount() * getColumnCount();
    }
    default boolean isSquare() {
        return getRowCount() == getColumnCount();
    }
    /* END-JAVA-8 */ /* BEGIN-POLYFILL-6 *../ int getElementCount(); boolean isSquare(); /..* END-POLYFILL-6 */


    default VectorElementIterator getRowElements(int row) {
        return getFilteredRowElements(row, null);
    }

    default VectorElementIterator getFilteredRowElements(int row, DoublePredicate filter) {
        if (row < 0 || row >= getRowCount()) throw new IllegalArgumentException();

        Matrix outer = this;
        return new VectorElementIterator() {
            private double value;
            private int cur = -1;
            private boolean isValid = false;

            @Override
            public double getValue() {
                if (cur < 0) throw new NoSuchElementException();
                return value;
            }

            @Override
            public double setValue(double value) {
                if (cur < 0) throw new NoSuchElementException();
                this.value = value;
                return outer.set(row, cur, value);
            }

            @Override
            public boolean hasNext() {
                while (!isValid) {
                    if (++cur >= getColumnCount())
                        return false;
                    value = outer.get(row, cur);
                    isValid = filter == null || filter.test(value);
                }
                return true;
            }

            @Override
            public int nextInt() {
                if (!hasNext()) throw new NoSuchElementException();
                isValid = false;
                return cur;
            }
        };
    }

    default VectorElementIterator getColumnElements(int column) {
        return getFilteredColumnElements(column, null);
    }

    default VectorElementIterator getFilteredColumnElements(int column, DoublePredicate filter) {
        if (column < 0 || column >= getColumnCount()) throw new IllegalArgumentException();

        Matrix outer = this;
        return new VectorElementIterator() {
            private double value;
            private int cur = -1;
            private boolean isValid = false;

            @Override
            public double getValue() {
                if (cur < 0) throw new NoSuchElementException();
                return value;
            }

            @Override
            public double setValue(double value) {
                if (cur < 0) throw new NoSuchElementException();
                this.value = value;
                return outer.set(cur, column, value);
            }

            @Override
            public boolean hasNext() {
                while (!isValid) {
                    if (++cur >= getColumnCount())
                        return false;
                    value = outer.get(cur, column);
                    isValid = filter == null || filter.test(value);
                }
                return true;
            }

            @Override
            public int nextInt() {
                if (!hasNext()) throw new NoSuchElementException();
                isValid = false;
                return cur;
            }
        };
    }

    default VectorElementIterator getRowNonZeroes(int row) {
        return getRowNonValued(row, 0);
    }
    default VectorElementIterator getRowNonValued(int row, double value) {
        return getFilteredRowElements(row, a -> !MathUtils.doubleEquals(a, value));
    }
    default VectorElementIterator getColumnNonZeroes(int column) {
        return getColumnNonValued(column, 0);
    }
    default VectorElementIterator getColumnNonValued(int column, double value) {
        return getFilteredColumnElements(column, a -> !MathUtils.doubleEquals(a, value));
    }


    default Matrix transposeView() {
        return new TransposeMatrix(this);
    }


    default boolean isSparse() {
        return false;
    }


    /**
     * Returns an iterator iterating through all non-zero elements in this matrix.
     */
    @Override
    default Iterator<Triple<Integer, Integer, Double>> iterator() {
        return new Iterator<Triple<Integer, Integer, Double>>() {
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
                Triple<Integer, Integer, Double> t = new Triple<>(i % getRowCount(), i / getRowCount(), result);
                i++;
                return t;
            }
        };
    }







    static <T extends Matrix> T combine(Matrix a, Matrix b, Supplier<T> matrixSupplier, BiFunction<Double, Double, Double> combiner) {
        if (a.getRowCount() != b.getRowCount() || a.getColumnCount() != b.getColumnCount())
            throw new IllegalArgumentException("Matrices must have the same dimensions!");

        T result = matrixSupplier.get();
        boolean isDense = !(a instanceof SparseMatrix && b instanceof SparseMatrix);

        if (isDense) {
            for (int i = 0; i < result.getRowCount(); i++) {
                for (int j = 0; j < result.getColumnCount(); j++) {
                    result.set(i, j, combiner.apply(a.get(i, j), b.get(i, j)));
                }
            }
        } else {
            // TODO Right now it's just the dense code copy-pasted; use the fact we're using a sparse matrix to optimize instead
            for (int i = 0; i < result.getRowCount(); i++) {
                for (int j = 0; j < result.getColumnCount(); j++) {
                    result.set(i, j, combiner.apply(a.get(i, j), b.get(i, j)));
                }
            }
        }

        return result;
    }

}
