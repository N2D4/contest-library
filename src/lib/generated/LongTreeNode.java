/* Generated by Generificator on Fri Nov 08 00:29:31 CET 2019 from TreeNode.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.trees.*;

import lib.algorithms.O;
import lib.utils.QueueUtils;
import lib.utils.Utils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;


public class LongTreeNode implements Serializable {
    private long value;
    private int height;
    private double distance;
    private double distanceToParent;
    private List<LongTreeNode> children;
    private LongTreeNode parent;
    private LongTree tree;

    public LongTreeNode(long value) {
        this(value, null);
    }

    protected LongTreeNode(long value, LongTree tree) {
        this.value = value;
        this.parent = null;
        this.tree = tree;
        this.children = new ArrayList<LongTreeNode>(5);
        this.height = 0;
        this.distance = tree.distanceFolder.a;
    }

    protected LongTreeNode(long value, LongTreeNode parent, double distanceToParent) {
        this.value = value;
        this.parent = parent;
        this.tree = parent.tree;
        this.children = new ArrayList<LongTreeNode>();
        this.height = parent.getHeight() + 1;
        this.distanceToParent = distanceToParent;
        this.distance = tree.distanceFolder.b.applyAsDouble(parent.getDistance(), distanceToParent);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public int getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    public double getDistanceToParent() {
        if (!hasParent()) throw new NoSuchElementException("Node has no parent!");
        return this.distanceToParent;
    }

    public List<LongTreeNode> getChildren() {
        return this.children.size() == 0 ? Collections.emptyList() : Collections.unmodifiableList(children);
    }

    public int getChildCount() {
        return children.size();
    }

    public LongTreeNode addChild(long value) {
        return addChild(value, 1);
    }

    public LongTreeNode addChild(long value, double distance) {
        return createUnattached(value, distance).attach();
    }

    public LongTreeNode.Unattached createUnattached(long value, double distance) {
        return new Unattached(value, distance);
    }

    public class Unattached extends LongTreeNode {
        private boolean isAttached = false;

        private Unattached(long value, double distance) {
            super(value, LongTreeNode.this, distance);
        }

        public LongTreeNode attach() {
            if (isAttached()) throw new NoSuchElementException("Node has already been attached!");
            LongTreeNode.this.children.add(this);
            isAttached = true;
            return this;
        }

        @Override
        public boolean isAttached() {
            return isAttached;
        }
    }

    public LongTreeNode getParent() {
        return parent;
    }

    public List<LongTreeNode> getParentChain() {
        List<LongTreeNode> res = new ArrayList<LongTreeNode>();
        LongTreeNode node = this;
        while ((node = node.getParent()) != null) {
            res.add(node);
        }
        Collections.reverse(res);
        return res;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public LongTree getTree() {
        return tree;
    }

    public boolean isAttached() {
        return true;
    }





    @O("n")
    public List<LongTreeNode> leafNodes() {
        return traverse(2);
    }

    @O("n")
    public List<LongTreeNode> preOrder() {
        return traverse(0);
    }

    @O("n")
    public List<LongTreeNode> postOrder() {
        return traverse(1);
    }


    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private List<LongTreeNode> traverse(int mode) {
        List<LongTreeNode> list = new ArrayList<LongTreeNode>();
        traverse(list, mode);
        return list;
    }

    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private void traverse(List<LongTreeNode> list, int mode) {
        Queue<LongTreeNode> queue = QueueUtils.createLIFO();
        queue.add(this);

        while (!queue.isEmpty()) {
            LongTreeNode n = queue.remove();
            if (mode != 2 || n.getChildCount() == 0) list.add(n);

            Iterable<LongTreeNode> children = mode == 1 ? n.getChildren() : Utils.reverseIterable(n.getChildren());
            for (LongTreeNode child : children) {
                queue.add(child);
            }
        }

        if (mode == 1) Collections.reverse(list);
    }


    @Override
    public String toString() {
        return ("" + value) + (children.isEmpty() ? "" : children.toString());
    }
}
