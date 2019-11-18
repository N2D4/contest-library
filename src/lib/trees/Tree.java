package lib.trees;

import lib.algorithms.O;
import lib.utils.tuples.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/* GENERIFY-THIS */
public class Tree<T> implements Serializable {
    private TreeNode<T> root;
    final Pair<Double, DoubleBinaryOperator> distanceFolder;

    public Tree(T rootValue) {
        this(rootValue, null);
    }

    public Tree(T rootValue, Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this(distanceFolder);
        initRoot(new TreeNode<T>(rootValue, this));
    }

    protected Tree() {
        this((Pair<Double, DoubleBinaryOperator>) null);
    }

    protected Tree(Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this.distanceFolder = distanceFolder == null ? new Pair<Double, DoubleBinaryOperator>(0.0, Double::sum) : distanceFolder;
    }

    protected void initRoot(TreeNode<T> node) {
        if (root != null) throw new IllegalStateException("Can't modify the root after it has already been initialized!");
        this.root = node;
    }

    public TreeNode<T> getRoot() {
        return root;
    }



    @O("n")
    public List<TreeNode<T>> leafNodes() {
        return getRoot().leafNodes();
    }

    @O("n")
    public List<TreeNode<T>> preOrder() {
        return getRoot().preOrder();
    }

    @O("n")
    public List<TreeNode<T>> postOrder() {
        return getRoot().postOrder();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
