/* BEGIN-NO-BUNDLE */

package lib.ntrees;

import lib.utils.tuples.Pair;
import lib.utils.various.Structure;

import java.util.Collections;
import java.util.List;

/**
 * A tree modifier.
 */
public abstract class TreeModifier<T extends TreeModifier.Tag> {
    /**
     * Returns the sentinel used to uniquely identify this modifier (no two modifiers with the same sentinel can be
     * on the same tree).
     *
     * Commonly returns itself, this modifier's class object, or a pair containing the class object and some additional
     * data.
     */
    protected Object getUniqueSentinel() {
        return this;
    }

    /**
     * Returns the number of long integers associated with this modifier on every node.
     */
    protected abstract int getLongDataSize();

    /**
     * Returns the number of objects associated with this modifier on every node.
     */
    protected abstract int getObjectDataSize();

    protected abstract T createTag(TagConstructorData data);

    protected abstract ElementUpdatePolicy getElementUpdatePolicy();

    protected List<Pair<TreeModifier<?>, ForeignElementUpdatePolicy>> getDependencies() {
        return Collections.emptyList();
    }


    public enum ElementUpdatePolicy {
        NONE,
        ON_CHILD_UPDATE,
        ON_PARENT_UPDATE
    }

    public enum ForeignElementUpdatePolicy {
        NONE,
        ON_SELF_UPDATE,
        ON_CHILD_UPDATE,
        ON_PARENT_UPDATE
    }

    public static abstract class Tag {
        private int longOffset;
        private int objOffset;
        private int longSize;
        private int objSize;
        private final List<TreeModifier.Tag> dependencies;

        protected Tag(TagConstructorData data) {
            this.longOffset = data.longOffset;
            this.objOffset = data.objOffset;
            this.longSize = data.longSize;
            this.objSize = data.objSize;
            this.dependencies = data.dependencies;
        }

        protected List<TreeModifier.Tag> getDependencies() {
            return Collections.unmodifiableList(dependencies);
        }

        protected long getLong(NTreeNode node, int index) {
            if (index < 0 || index >= longSize) throw new IllegalArgumentException("Index out of bounds!");
            return node.longData[longOffset + index];
        }

        protected Object getObject(NTreeNode node, int index) {
            if (index < 0 || index >= objSize) throw new IllegalArgumentException("Index out of bounds!");
            return node.objData[objOffset + index];
        }
    }

    public static final class TagConstructorData extends Structure {
        private int longOffset;
        private int objOffset;
        private int longSize;
        private int objSize;
        private List<TreeModifier.Tag> dependencies;
    }
}
/* END-NO-BUNDLE */
