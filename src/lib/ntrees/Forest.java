/* BEGIN-NO-BUNDLE */

package lib.ntrees;

import lib.utils.tuples.Pair;

import java.util.*;

/**
 * Represents a forest (multiple trees).
 */
public class Forest {
    private final Set<NTreeNode> nodes = Collections.newSetFromMap(new WeakHashMap<>());
    private int longDataSize = 0;
    private int objDataSize = 0;

    public Forest() {
        // Quite dusty here...
    }

    public <T extends TreeModifier.Tag> T addModifier(TreeModifier<T> modifier) {
        List<TreeModifier.Tag> dependencies = new ArrayList<>();
        for (Pair<TreeModifier<?>, TreeModifier.ForeignElementUpdatePolicy> pair : modifier.getDependencies()) {
            addModifier(pair.a);
            throw new RuntimeException("Add foreign update policy hook");
        }
        int lds = modifier.getLongDataSize();
        int ods = modifier.getObjectDataSize();
        throw new RuntimeException("unimplemented");
    }

    public NTreeNode createNode() {
        NTreeNode node = new NTreeNode(Forest.class, this);
        nodes.add(node);
        return node;
    }
}

/* END-NO-BUNDLE */
