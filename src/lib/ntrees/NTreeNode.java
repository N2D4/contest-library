/* BEGIN-NO-BUNDLE */

package lib.ntrees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NTreeNode {
    private static final long[] emptyLongArr = new long[0];
    private static final Object[] emptyObjArr = new Object[0];

    private Forest forest;
    private NTreeNode parent = null;
    private List<NTreeNode> children = new ArrayList<>(2);

    long[] longData = emptyLongArr;
    Object[] objData = emptyObjArr;


    NTreeNode(Object accessCheckSentinel, Forest forest) {
        if (accessCheckSentinel != Forest.class) throw new IllegalArgumentException("Please use Forest.createNode() instead of this constructor directly!");
        this.forest = forest;
    }

    public List<NTreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }
}

/* END-NO-BUNDLE */

