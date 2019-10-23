package lib.trees;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class TreeNode<T> implements Serializable {
    private T value;
    private int height;
    private double distance;
    private double distanceToParent;
    private List<TreeNode<T>> children;
    private TreeNode<T> parent;
    private Tree<T> tree;
    private Map<String, Object> attributeMap = null;

    public TreeNode(T value) {
        this(value, null);
    }

    protected TreeNode(T value, Tree<T> tree) {
        this.value = value;
        this.parent = null;
        this.tree = tree;
        this.children = new ArrayList<>(5);
        this.height = 0;
        this.distance = tree.distanceFolder.a;
    }

    protected TreeNode(T value, TreeNode<T> parent, double distanceToParent) {
        this.value = value;
        this.parent = parent;
        this.tree = parent.tree;
        this.children = new ArrayList<>();
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

    public List<TreeNode<T>> getParentChain() {
        List<TreeNode<T>> res = new ArrayList<>();
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

    /**
     * Returns a modifiable map of attributes that can be used to store additional information in this tree node.
     */
    public Map<String, Object> getAttributeMap() {
        if (attributeMap == null) attributeMap = new HashMap<>(3);
        return attributeMap;
    }

    /**
     * Either returns the value of the attribute if it is available in the attribute map, or uses the folding function
     * to collect it from the inheritance chain.
     */
    public <U> U inheritAttribute(String attrName, U firstValue, BiFunction<U, TreeNode<T>, U> fold) {
        return (U) getAttributeMap().computeIfAbsent(attrName, k -> {
            // No recursion due to possible stack overflows
            U val = firstValue;
            for (TreeNode<T> node : getParentChain()) {
                Map<String, Object> map = node.getAttributeMap();
                if (map.containsKey(attrName)) return map.get(attrName);
                val = fold.apply(val, node);
            }
            return firstValue;
        });
    }

    @Override
    public String toString() {
        return value.toString() + (children.isEmpty() ? "" : children.toString());
    }
}
