package lib.trees;

import lib.algorithms.O;
import lib.utils.QueueUtils;
import lib.utils.Utils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/* GENERIFY-THIS */
public class TreeNode<T> implements Serializable {
    private T value;
    private int height;
    private double distance;
    private double distanceToParent;
    private List<TreeNode<T>> children;
    private TreeNode<T> parent;
    private Tree<T> tree;

    public TreeNode(T value) {
        this(value, null);
    }

    protected TreeNode(T value, Tree<T> tree) {
        this.value = value;
        this.parent = null;
        this.tree = tree;
        this.children = new ArrayList<TreeNode<T>>(5);
        this.height = 0;
        this.distance = tree.distanceFolder.a;
    }

    protected TreeNode(T value, TreeNode<T> parent, double distanceToParent) {
        this.value = value;
        this.parent = parent;
        this.tree = parent.tree;
        this.children = new ArrayList<TreeNode<T>>();
        this.height = parent.getHeight() + 1;
        this.distanceToParent = distanceToParent;
        this.distance = tree.distanceFolder.b.applyAsDouble(parent.getDistance(), distanceToParent);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
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

    public List<TreeNode<T>> getChildren() {
        return this.children.size() == 0 ? Collections.emptyList() : Collections.unmodifiableList(children);
    }

    public int getChildCount() {
        return children.size();
    }

    public TreeNode<T> addChild(T value) {
        return addChild(value, 1);
    }

    public TreeNode<T> addChild(T value, double distance) {
        return createUnattached(value, distance).attach();
    }

    public TreeNode<T>.Unattached createUnattached(T value, double distance) {
        return new Unattached(value, distance);
    }

    public class Unattached extends TreeNode<T> {
        private boolean isAttached = false;

        private Unattached(T value, double distance) {
            super(value, /*PREFIX T*/TreeNode.this, distance);
        }

        public TreeNode<T> attach() {
            if (isAttached()) throw new NoSuchElementException("Node has already been attached!");
            /*PREFIX T*/TreeNode.this.children.add(this);
            isAttached = true;
            return this;
        }

        @Override
        public boolean isAttached() {
            return isAttached;
        }
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public List<TreeNode<T>> getParentChain() {
        List<TreeNode<T>> res = new ArrayList<TreeNode<T>>();
        TreeNode<T> node = this;
        while ((node = node.getParent()) != null) {
            res.add(node);
        }
        Collections.reverse(res);
        return res;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    public Tree<T> getTree() {
        return tree;
    }

    public boolean isAttached() {
        return true;
    }





    @O("n")
    public List<TreeNode<T>> leafNodes() {
        return traverse(2);
    }

    @O("n")
    public List<TreeNode<T>> preOrder() {
        return traverse(0);
    }

    @O("n")
    public List<TreeNode<T>> postOrder() {
        return traverse(1);
    }


    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private List<TreeNode<T>> traverse(int mode) {
        List<TreeNode<T>> list = new ArrayList<TreeNode<T>>();
        Queue<TreeNode<T>> queue = QueueUtils.createLIFO();
        queue.add(this);

        while (!queue.isEmpty()) {
            TreeNode<T> n = queue.remove();
            if (mode != 2 || n.getChildCount() == 0) list.add(n);

            Iterable<TreeNode<T>> children = mode == 1 ? n.getChildren() : Utils.reverseIterable(n.getChildren());
            for (TreeNode<T> child : children) {
                queue.add(child);
            }
        }

        if (mode == 1) Collections.reverse(list);

        return list;
    }


    @Override
    public String toString() {
        return ("" + value) + (children.isEmpty() ? "" : children.toString());
    }
}
