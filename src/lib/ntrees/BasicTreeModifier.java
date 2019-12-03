/* BEGIN-NO-BUNDLE */

package lib.ntrees;

import lib.utils.function.Func;

/* GENERIFY-THIS */
public class BasicTreeModifier<T> extends TreeModifier<BasicTreeModifier<T>.Tag> {
    private final Func<NTreeNode, T> update;
    private final ElementUpdatePolicy policy;

    public BasicTreeModifier(Func<NTreeNode, T> update, ElementUpdatePolicy policy) {
        this.update = update;
        this.policy = policy;
    }

    private boolean isPrimitive() {
        /*IS-PRIMITIVE T./
            return true;
        /.ELSE*/
            return false;
        /*END*/
    }

    @Override
    protected int getLongDataSize() {
        return isPrimitive() ? 1 : 0;
    }

    @Override
    protected int getObjectDataSize() {
        return isPrimitive() ? 0 : 1;
    }

    @Override
    protected Tag createTag(TagConstructorData data) {
        return new Tag(data);
    }

    @Override
    protected ElementUpdatePolicy getElementUpdatePolicy() {
        return policy;
    }

    public class Tag extends TreeModifier.Tag {
        protected Tag(TagConstructorData data) {
            super(data);
        }

        public T get(NTreeNode node) {
            /*IS-PRIMITIVE T./
                return (T) getLong(node, 0);
            /.ELSE*/
                return (T) getObject(node, 0);
            /*END*/
        }
    }
}

/* END-NO-BUNDLE */
