/* Generated by Generificator on Tue Dec 03 22:32:59 CET 2019 from BasicTreeModifier.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
/* BEGIN-NO-BUNDLE */

import lib.ntrees.*;

import lib.utils.function.Func;


public class IntBasicTreeModifier extends TreeModifier<IntBasicTreeModifier.Tag> {
    private final ObjIntFunc<NTreeNode> update;
    private final ElementUpdatePolicy policy;

    public IntBasicTreeModifier(ObjIntFunc<NTreeNode> update, ElementUpdatePolicy policy) {
        this.update = update;
        this.policy = policy;
    }

    private boolean isPrimitive() {
        
            return true;
        
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

        public int get(NTreeNode node) {
            
                return (int) getLong(node, 0);
            
        }
    }
}

/* END-NO-BUNDLE */
