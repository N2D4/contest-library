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


public class DoubleTreeNode implements Serializable {
    private double value;
    private int height;
    private double distance;
    private double distanceToParent;
    private List<DoubleTreeNode> children;
    private DoubleTreeNode parent;
    private DoubleTree tree;

    public DoubleTreeNode(double value) {
        this(value, null);
    }

    protected DoubleTreeNode(double value, DoubleTree tree) {
        this.value = value;
        this.parent = null;
        this.tree = tree;
        this.children = new ArrayList<DoubleTreeNode>(5);
        this.height = 0;
        this.distance = tree.distanceFolder.a;
    }

    protected DoubleTreeNode(double value, DoubleTreeNode parent, double distanceToParent) {
        this.value = value;
        this.parent = parent;
        this.tree = parent.tree;
        this.children = new ArrayList<DoubleTreeNode>();
        this.height = parent.getHeight() + 1;
        this.distanceToParent = distanceToParent;
        this.distance = tree.distanceFolder.b.applyAsDouble(parent.getDistance(), distanceToParent);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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

    public List<DoubleTreeNode> getChildren() {
        return this.children.size() == 0 ? Collections.emptyList() : Collections.unmodifiableList(children);
    }

    public int getChildCount() {
        return children.size();
    }

    public DoubleTreeNode addChild(double value) {
        return addChild(value, 1);
    }

    public DoubleTreeNode addChild(double value, double distance) {
        return createUnattached(value, distance).attach();
    }

    public DoubleTreeNode.Unattached createUnattached(double value, double distance) {
        return new Unattached(value, distance);
    }

    public class Unattached extends DoubleTreeNode {
        private boolean isAttached = false;

        private Unattached(double value, double distance) {
            super(value, DoubleTreeNode.this, distance);
        }

        public DoubleTreeNode attach() {
            if (isAttached()) throw new NoSuchElementException("Node has already been attached!");
            DoubleTreeNode.this.children.add(this);
            isAttached = true;
            return this;
        }

        @Override
        public boolean isAttached() {
            return isAttached;
        }
    }

    public DoubleTreeNode getParent() {
        return parent;
    }

    public List<DoubleTreeNode> getParentChain() {
        List<DoubleTreeNode> res = new ArrayList<DoubleTreeNode>();
        DoubleTreeNode node = this;
        while ((node = node.getParent()) != null) {
            res.add(node);
        }
        Collections.reverse(res);
        return res;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public DoubleTree getTree() {
        return tree;
    }

    public boolean isAttached() {
        return true;
    }





    @O("n")
    public List<DoubleTreeNode> leafNodes() {
        return traverse(2);
    }

    @O("n")
    public List<DoubleTreeNode> preOrder() {
        return traverse(0);
    }

    @O("n")
    public List<DoubleTreeNode> postOrder() {
        return traverse(1);
    }


    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private List<DoubleTreeNode> traverse(int mode) {
        List<DoubleTreeNode> list = new ArrayList<DoubleTreeNode>();
        traverse(list, mode);
        return list;
    }

    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private void traverse(List<DoubleTreeNode> list, int mode) {
        Queue<DoubleTreeNode> queue = QueueUtils.createLIFO();
        queue.add(this);

        while (!queue.isEmpty()) {
            DoubleTreeNode n = queue.remove();
            if (mode != 2 || n.getChildCount() == 0) list.add(n);

            Iterable<DoubleTreeNode> children = mode == 1 ? n.getChildren() : Utils.reverseIterable(n.getChildren());
            for (DoubleTreeNode child : children) {
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