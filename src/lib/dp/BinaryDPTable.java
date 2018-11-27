package lib.dp;

public abstract class BinaryDPTable<T> extends DPTable<T> {

    public BinaryDPTable(int xSize, int ySize) {
        super(xSize, ySize);
    }

    @Override
    protected T calc(int[] p) {
        return calc(p[0], p[1]);
    }

    @Override
    protected T getOutOfRange(int[] p) {
        return getOutOfRange(p[0], p[1]);
    }


    protected abstract T calc(int i, int j);
    protected abstract T getOutOfRange(int i, int j);
}
