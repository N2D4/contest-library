package lib.dp;

public abstract class TernaryDPTable<T> extends DPTable<T> {

    public TernaryDPTable(int xSize, int ySize, int zSize) {
        super(xSize, ySize, zSize);
    }

    @Override
    protected T calc(int[] p) {
        return calc(p[0], p[1], p[2]);
    }

    @Override
    protected T getOutOfRange(int[] p) {
        return getOutOfRange(p[0], p[1], p[2]);
    }


    protected abstract T calc(int i, int j, int k);
    protected abstract T getOutOfRange(int i, int j, int k);
}
