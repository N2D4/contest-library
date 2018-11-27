package lib.vectorization;

import java.util.function.BiFunction;

public interface ResizableMatrix extends Matrix {
    void addRow(int index);
    /* BEGIN-JAVA-8 */
    default void addRow() {
        addRow(this.getRowCount());
    }
    /* END-JAVA-8 */
    void removeRow(int index);

    void addColumn(int index);
    /* BEGIN-JAVA-8 */
    default void addColumn() {
        addRow(this.getColumnCount());
    }
    /* END-JAVA-8 */
    void removeColumn(int index);


    @Override
    default ResizableMatrix transposeView() {
        return new TransposeMatrix.Resizable(this);
    }






    static ResizableMatrix combine(Matrix a, Matrix b, BiFunction<Double, Double, Double> combiner) {
        if (a.getRowCount() != b.getRowCount() || a.getColumnCount() != b.getColumnCount())
            throw new IllegalArgumentException("Matrices must have the same dimensions!");

        ResizableMatrix result;
        boolean isDense = false;
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {         // TODO better detection; check for row/column sparseness
            result = new RowListSparseMatrix(a.getRowCount(), a.getColumnCount(), combiner.apply(((SparseMatrix) a).getSparseValue(), ((SparseMatrix) b).getSparseValue()));
        } else {
            isDense = true;
            result = new ArrayListMatrix(a.getRowCount(), a.getColumnCount());
        }

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
