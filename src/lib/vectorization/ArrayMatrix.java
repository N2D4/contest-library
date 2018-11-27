package lib.vectorization;

public class ArrayMatrix extends AbstractMatrix implements DenseMatrix {
    private final double[][] arr;
    private final int clmns;

    public ArrayMatrix(int rows, int columns) {
        this.arr = new double[rows][columns];
        this.clmns = columns;
    }

    public ArrayMatrix(Matrix fromMatrix) {
        this(fromMatrix.getRowCount(), fromMatrix.getColumnCount());
        AbstractMatrix.copyAllNon(fromMatrix, this, 0);
    }

    @Override
    public double get(int row, int column) {
        return arr[row][column];
    }

    @Override
    public double set(int row, int column, double value) {
        double old = arr[row][column];
        arr[row][column] = value;
        return old;
    }

    @Override
    public int getRowCount() {
        return arr.length;
    }

    @Override
    public int getColumnCount() {
        return this.clmns;
    }
}
