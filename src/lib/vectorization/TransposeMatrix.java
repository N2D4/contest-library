package lib.vectorization;

import lib.utils.tuples.Triple;

import java.util.Iterator;
import java.util.function.DoublePredicate;

// TODO: TransposeMatrix.Sparse and TransposeMatrix.Resizable.Sparse
class TransposeMatrix extends AbstractMatrix {

    private Matrix matrix;

    public TransposeMatrix(Matrix original) {
        matrix = original;
    }


    public static class Resizable extends TransposeMatrix implements ResizableMatrix {

        private ResizableMatrix resizable;

        public Resizable(ResizableMatrix original) {
            super(original);
            this.resizable = original;
        }

        @Override
        public void addRow(int index) {
            resizable.addColumn(index);
        }

        @Override
        public void removeRow(int index) {
            resizable.removeColumn(index);
        }

        @Override
        public void addColumn(int index) {
            resizable.addRow(index);
        }

        @Override
        public void removeColumn(int index) {
            resizable.removeRow(index);
        }

        @Override
        public ResizableMatrix transposeView() {
            return resizable;
        }
    }




    @Override
    public double get(int row, int column) {
        return matrix.get(column, row);
    }

    @Override
    public double set(int row, int column, double value) {
        return matrix.set(column, row, value);
    }

    @Override
    public void setAll(double value) {
        matrix.setAll(value);
    }

    @Override
    public int getRowCount() {
        return matrix.getColumnCount();
    }

    @Override
    public int getColumnCount() {
        return matrix.getRowCount();
    }

    @Override
    public int getElementCount() {
        return matrix.getElementCount();
    }

    @Override
    public boolean isSquare() {
        return matrix.isSquare();
    }

    @Override
    public VectorElementIterator getRowElements(int row) {
        return matrix.getColumnElements(row);
    }

    @Override
    public VectorElementIterator getFilteredRowElements(int row, DoublePredicate filter) {
        return matrix.getFilteredColumnElements(row, filter);
    }

    @Override
    public VectorElementIterator getColumnElements(int column) {
        return matrix.getRowElements(column);
    }


    @Override
    public VectorElementIterator getFilteredColumnElements(int column, DoublePredicate filter) {
        return matrix.getFilteredRowElements(column, filter);
    }

    @Override
    public VectorElementIterator getRowNonZeroes(int row) {
        return matrix.getColumnNonZeroes(row);
    }

    @Override
    public VectorElementIterator getRowNonValued(int row, double value) {
        return matrix.getColumnNonValued(row, value);
    }

    @Override
    public VectorElementIterator getColumnNonZeroes(int column) {
        return matrix.getRowNonZeroes(column);
    }

    @Override
    public VectorElementIterator getColumnNonValued(int column, double value) {
        return matrix.getRowNonValued(column, value);
    }

    @Override
    public Matrix transposeView() {
        return matrix;
    }

    @Override
    public boolean isSparse() {
        return matrix.isSparse();
    }

    @Override
    public Iterator<Triple<Integer, Integer, Double>> iterator() {
        return matrix.iterator();
    }
}
