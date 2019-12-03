/* Generated by Generificator on Tue Dec 03 15:26:20 CET 2019 from TreeNode.java */

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


public class IntTreeNode implements Serializable {
    private int value;
    private int height;
    private double distance;
    private double distanceToParent;
    private List<IntTreeNode> children;
    private IntTreeNode parent;
    private IntTree tree;

    public IntTreeNode(int value) {
        this(value, null);
    }

    protected IntTreeNode(int value, IntTree tree) {
        this.value = value;
        this.parent = null;
        this.tree = tree;
        this.children = new ArrayList<IntTreeNode>(5);
        this.height = 0;
        this.distance = tree.distanceFolder.a;
    }

    protected IntTreeNode(int value, IntTreeNode parent, double distanceToParent) {
        this.value = value;
        this.parent = parent;
        this.tree = parent.tree;
        this.children = new ArrayList<IntTreeNode>();
        this.height = parent.getHeight() + 1;
        this.distanceToParent = distanceToParent;
        this.distance = tree.distanceFolder.b.applyAsDouble(parent.getDistance(), distanceToParent);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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

    public List<IntTreeNode> getChildren() {
        return this.children.size() == 0 ? Collections.emptyList() : Collections.unmodifiableList(children);
    }

    public int getChildCount() {
        return children.size();
    }

    public IntTreeNode addChild(int value) {
        return addChild(value, 1);
    }

    public IntTreeNode addChild(int value, double distance) {
        return createUnattached(value, distance).attach();
    }

    public IntTreeNode.Unattached createUnattached(int value, double distance) {
        return new Unattached(value, distance);
    }

    public class Unattached extends IntTreeNode {
        private boolean isAttached = false;

        private Unattached(int value, double distance) {
            super(value, IntTreeNode.this, distance);
        }

        public IntTreeNode attach() {
            if (isAttached()) throw new NoSuchElementException("Node has already been attached!");
            IntTreeNode.this.children.add(this);
            isAttached = true;
            return this;
        }

        @Override
        public boolean isAttached() {
            return isAttached;
        }
    }

    public IntTreeNode getParent() {
        return parent;
    }

    public List<IntTreeNode> getParentChain() {
        List<IntTreeNode> res = new ArrayList<IntTreeNode>();
        IntTreeNode node = this;
        while ((node = node.getParent()) != null) {
            res.add(node);
        }
        Collections.reverse(res);
        return res;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public IntTree getTree() {
        return tree;
    }

    public boolean isAttached() {
        return true;
    }





    @O("n")
    public List<IntTreeNode> leafNodes() {
        return traverse(2);
    }

    @O("n")
    public List<IntTreeNode> preOrder() {
        return traverse(0);
    }

    @O("n")
    public List<IntTreeNode> postOrder() {
        return traverse(1);
    }


    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private List<IntTreeNode> traverse(int mode) {
        List<IntTreeNode> list = new ArrayList<IntTreeNode>();
        traverse(list, mode);
        return list;
    }

    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private void traverse(List<IntTreeNode> list, int mode) {
        Queue<IntTreeNode> queue = QueueUtils.createLIFO();
        queue.add(this);

        while (!queue.isEmpty()) {
            IntTreeNode n = queue.remove();
            if (mode != 2 || n.getChildCount() == 0) list.add(n);

            Iterable<IntTreeNode> children = mode == 1 ? n.getChildren() : Utils.reverseIterable(n.getChildren());
            for (IntTreeNode child : children) {
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
