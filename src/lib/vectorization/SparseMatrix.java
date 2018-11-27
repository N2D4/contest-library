package lib.vectorization;

import lib.utils.MathUtils;

public interface SparseMatrix extends Matrix {
    double getSparseValue();
    /* BEGIN-JAVA-8 */
    default boolean isSparseValue(double d) {
        return MathUtils.doubleEquals(d, getSparseValue());
    }
    /* END-JAVA-8 */
    /* BEGIN-POLYFILL-6 *../
    boolean isSparseValue(double d);
    /..* END-POLYFILL-6 */
    void clear();


    @Override
    default boolean isSparse() {
        return true;
    }
}
