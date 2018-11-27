package lib.dp;

import lib.utils.MathUtils;
import lib.utils.various.BruteForceIterable;

import java.io.Serializable;
import java.util.Arrays;

public abstract class DPTable<T> implements Serializable {
    private static final Object UNASSIGNED = new Object() {
        @Override
        public String toString() {
            return "unassigned";
        }
    };

    private int[] dimensions;
    private Object[] DP;

    public DPTable(int... dimensions) {
        this.dimensions = dimensions;
        int size = MathUtils.prod(dimensions);
        this.DP = new Object[size];
        for (int i = 0; i < DP.length; i++) {
            DP[i] = DPTable.UNASSIGNED;
        }
    }

    public T get(int... position) {
        for (int i = 0; i < position.length; i++) {
            if (position[i] >= dimensions[i] || position[i] < 0) {
                return getOutOfRange(position);
            }
        }
        int p = MathUtils.mergeIntoInteger(position, dimensions);
        if (DP[p] == DPTable.UNASSIGNED) DP[p] = calc(position);
        return (T) DP[p];
    }


    private T getRaw(int p) {
        if (DP[p] == DPTable.UNASSIGNED) DP[p] = calc(MathUtils.splitIntoArray(p, dimensions));
        return (T) DP[p];
    }

    public void calculateAll() {
        for (int i = 0; i < DP.length; i++) {
            getRaw(i);
        }
    }


    @Override
    public String toString() {
        return "DPTable with dimensions " + Arrays.toString(dimensions) + "\n" + Arrays.deepToString(DP);
    }


    protected abstract void initialize();
    protected abstract T calc(int[] p);
    protected abstract T getOutOfRange(int[] position);
}
