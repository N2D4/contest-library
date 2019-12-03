/* Generated by Generificator on Tue Dec 03 15:26:21 CET 2019 from BasicTreeModifier.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
/* BEGIN-NO-BUNDLE */

import lib.ntrees.*;

import lib.utils.function.Func;

import java.util.function.Function;


public class LongBasicTreeModifier extends TreeModifier<LongBasicTreeModifier.Tag> {
    private final ObjLongFunc<TreeNode> update;
    private final ElementUpdatePolicy policy;

    public LongBasicTreeModifier(ObjLongFunc<TreeNode> update, ElementUpdatePolicy policy) {
        this.update = update;
        this.policy = policy;
    }

    private boolean isPrimitive() {
        /*IS-PRIMITIVE long*/
            return true;
        /*ELSE./
            return false;
        /.END*/
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

        public long get(TreeNode node) {
            /*IS-PRIMITIVE long*/
                return (long) getLong(node, 0);
            /*ELSE./
                return (long) getObject(node, 0);
            /.END*/
        }
    }
}

/* END-NO-BUNDLE */
