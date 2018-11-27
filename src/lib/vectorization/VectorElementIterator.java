package lib.vectorization;

import lib.generated.IntIterator;

public interface VectorElementIterator extends IntIterator {
    double getValue();
    double setValue(double value);
}