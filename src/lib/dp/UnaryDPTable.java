package lib.dp;

public abstract class UnaryDPTable<T> extends DPTable<T> {

    public UnaryDPTable(int xSize) {
        super(xSize);
    }

    @Override
    protected T calc(int[] p) {
        return calc(p[0]);
    }

    @Override
    protected T getOutOfRange(int[] p) {
        return getOutOfRange(p[0]);
    }

    protected abstract T calc(int i);
    protected abstract T getOutOfRange(int i);
}
