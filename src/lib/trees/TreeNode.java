package lib.trees;

import java.io.Serializable;
import java.util.*;

public class TreeNode<T> implements Serializable {
    private T value;
    private int height;
    private double distance;
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
        this.children = new ArrayList<>();
        this.height = 0;
    }

    protected TreeNode(T value, TreeNode<T> parent, double distanceToParent) {
        this.value = value;
        this.parent = parent;
        this.tree = parent.tree;
        this.children = new ArrayList<>();
        this.height = parent.getHeight() + 1;
        this.distance = parent.getDistance() + distanceToParent;
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
        return getDistance() - parent.getDistance();
    }

    public List<TreeNode<T>> getChildren() {
        return Collections.unmodifiableList(children);
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
            super(value, TreeNode.this, distance);
        }

        public TreeNode<T> attach() {
            if (isAttached()) throw new NoSuchElementException("Node has already been attached!");
            TreeNode.this.children.add(this);
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

    public boolean hasParent() {
        return getParent() != null;
    }

    public Tree<T> getTree() {
        return tree;
    }

    public boolean isAttached() {
        return true;
    }

    @Override
    public String toString() {
        return value.toString() + (children.isEmpty() ? "" : children.toString());
    }
}
