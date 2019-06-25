package lib.vectorization;

import java.util.function.BiFunction;
import java.util.function.Supplier;

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
}
