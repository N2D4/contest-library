package lib.vectorization;

import lib.generated.DoubleArrayList;

import java.util.ArrayList;
import java.util.List;

public class ArrayListMatrix extends AbstractMatrix implements ResizableMatrix, DenseMatrix {

    private final List<DoubleArrayList> rows;
    private int clmnsCount = 0;


    public ArrayListMatrix() {
        this(0, 0);
    }

    public ArrayListMatrix(int initialRows, int initialColumns) {
        this.rows = new ArrayList<>(Math.max(initialRows, 10));
        this.clmnsCount = initialColumns;
        for (int i = 0; i < initialRows; i++) {
            addRow(/* BEGIN-POLYFILL-6 *../ getRowCount() /..* END-POLYFILL-6 */);
        }
    }

    public ArrayListMatrix(Matrix fromMatrix) {
        this(fromMatrix.getRowCount(), fromMatrix.getColumnCount());
        AbstractMatrix.copyAllNon(fromMatrix, this, 0);
    }


    @Override
    public void addRow(int index) {
        DoubleArrayList row = new DoubleArrayList(clmnsCount);
        for (int i = 0; i < clmnsCount; i++) {
            row.add(0d);
        }
        rows.add(index, row);
    }

    @Override
    public void removeRow(int index) {
        rows.remove(index);
    }

    @Override
    public void addColumn(int index) {
        for (DoubleArrayList row : rows) {
            row.add(index, 0d);
        }
        clmnsCount++;
    }

    @Override
    public void removeColumn(int index) {
        for (DoubleArrayList row : rows) {
            row.remove(index);
        }
        clmnsCount--;
    }

    @Override
    public double get(int row, int column) {
        return rows.get(row).get(column);
    }

    @Override
    public double set(int row, int column, double value) {
        return rows.get(row).set(column, value);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return clmnsCount;
    }
}
