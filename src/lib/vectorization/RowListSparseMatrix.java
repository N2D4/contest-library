package lib.vectorization;

import lib.polyfill.PolyfillIterator;
import lib.utils.tuples.Triple;

import java.util.*;

public class RowListSparseMatrix extends AbstractMatrix implements ResizableMatrix, SparseMatrix {

    private int clmnsCount;
    private final double sparseValue;
    private ArrayList<SparseVector> rows;
    private final SparseVector emptyRow;




    public RowListSparseMatrix() {
        this(0, 0);
    }

    public RowListSparseMatrix(int initialRows, int initialColumns) {
        this(initialRows, initialColumns, 0);
    }

    public RowListSparseMatrix(int initialRows, int initialColumns, double sparseValue) {
        this.sparseValue = sparseValue;
        this.rows = new ArrayList<>(Math.max(initialRows, 10));
        this.clmnsCount = initialColumns;
        emptyRow = SparseVector.empty(clmnsCount, getSparseValue());
        for (int i = 0; i < initialRows; i++) {
            addRow();
        }
    }

    public RowListSparseMatrix(Matrix fromMatrix, double sparseValue) {
        this(fromMatrix.getRowCount(), fromMatrix.getColumnCount(), sparseValue);
        AbstractMatrix.copyAllNon(fromMatrix, this, sparseValue);
    }



    @Override
    public void setAll(double value) {
        if (isSparseValue(value)) {
            clear();
        } else {
            super.setAll(value);
        }
    }

    @Override
    public double get(int row, int column) {
        rangeChecks(row, column);
        return rows.get(row).get(column);
    }

    private SparseVector getRowModifiable(int index) {
        SparseVector result = rows.get(index);
        if (result != emptyRow) {
            return result;
        } else {
            result = new SparseVector(clmnsCount, getSparseValue());
            rows.set(index, result);
            return result;
        }
    }

    @Override
    public double set(int row, int column, double value) {
        rangeChecks(row, column);
        return getRowModifiable(row).set(column, value);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return clmnsCount;
    }

    @Override
    public void addRow(int index) {
        if (index < 0 || index > getRowCount()) throw new IllegalArgumentException();

        rows.add(index, emptyRow);
    }

    @Override
    public void removeRow(int index) {
        if (index < 0 || index >= getRowCount()) throw new IllegalArgumentException();

        rows.remove(index);
    }

    @Override
    public void addColumn(int index) {
        if (index < 0 || index > getColumnCount()) throw new IllegalArgumentException();

        clmnsCount++;
        if (index == clmnsCount - 1) return;
        for (SparseVector vec : rows) {
            vec.add(index, getSparseValue());
        }
    }

    @Override
    public void removeColumn(int index) {
        if (index < 0 || index >= getColumnCount()) throw new IllegalArgumentException();

        clmnsCount--;
        for (SparseVector vec : rows) {
            vec.remove(index);
        }
    }

    @Override
    public VectorElementIterator getRowNonValued(int row, double value) {
        if (!isSparseValue(value)) return super.getRowNonValued(row, value);

        if (row < 0 || row >= getRowCount()) throw new IllegalArgumentException();
        return rows.get(row).iterator();
    }


    @Override
    public Iterator<Triple<Integer, Integer, Double>> iterator() {
        return new PolyfillIterator<Triple<Integer, Integer, Double>>() {
            int rowi = -1;
            VectorElementIterator colit;

            @Override
            public boolean hasNext() {
                while (colit == null || !colit.hasNext()) {
                    if (++rowi >= getRowCount()) return false;

                    colit = rows.get(rowi).iterator();
                }
                return true;
            }

            @Override
            public Triple<Integer, Integer, Double> next() {
                if (!hasNext()) throw new NoSuchElementException();
                int c = colit.next();
                return new Triple<>(rowi, c, colit.getValue());
            }
        };
    }

    @Override
    public double getSparseValue() {
        return sparseValue;
    }

    @Override
    public void clear() {
        for (SparseVector row : rows) {
            if (row == emptyRow) continue;
            row.clear();
        }
    }


    /* BEGIN-POLYFILL-6 *../
    @Override
    public boolean isSparseValue(double d) {
        return MathUtils.doubleEquals(d, getSparseValue());
    }
    /..* END-POLYFILL-6 */
}
