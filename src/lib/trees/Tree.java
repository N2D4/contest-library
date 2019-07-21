package lib.trees;

import lib.utils.tuples.Pair;

import java.io.Serializable;
import java.util.function.DoubleBinaryOperator;

public class Tree<T> implements Serializable {
    private TreeNode<T> root;
    final Pair<Double, DoubleBinaryOperator> distanceFolder;

    public Tree(T rootValue) {
        this(rootValue, null);
    }

    public Tree(T rootValue, Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this(distanceFolder);
        initRoot(new TreeNode(rootValue, this));
    }

    protected Tree() {
        this((Pair<Double, DoubleBinaryOperator>) null);
    }

    protected Tree(Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this.distanceFolder = distanceFolder == null ? new Pair<>(0.0, Double::sum) : distanceFolder;
    }

    protected void initRoot(TreeNode<T> node) {
        if (root != null) throw new IllegalStateException("Can't modify the root after it has already been initialized!");
        this.root = node;
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
