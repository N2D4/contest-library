/* Generated by Generificator from Tree.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.trees.*;

import lib.algorithms.O;
import lib.utils.tuples.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.function.DoubleBinaryOperator;


public class DoubleTree implements Serializable {
    private DoubleTreeNode root;
    final Pair<Double, DoubleBinaryOperator> distanceFolder;

    public DoubleTree(double rootValue) {
        this(rootValue, null);
    }

    public DoubleTree(double rootValue, Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this(distanceFolder);
        initRoot(new DoubleTreeNode(rootValue, this));
    }

    protected DoubleTree() {
        this((Pair<Double, DoubleBinaryOperator>) null);
    }

    protected DoubleTree(Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this.distanceFolder = distanceFolder == null ? new Pair<Double, DoubleBinaryOperator>(0.0, Double::sum) : distanceFolder;
    }

    protected void initRoot(DoubleTreeNode node) {
        if (root != null) throw new IllegalStateException("Can't modify the root after it has already been initialized!");
        this.root = node;
    }

    public DoubleTreeNode getRoot() {
        return root;
    }



    @O("n")
    public List<DoubleTreeNode> leafNodes() {
        return getRoot().leafNodes();
    }

    @O("n")
    public List<DoubleTreeNode> preOrder() {
        return getRoot().preOrder();
    }

    @O("n")
    public List<DoubleTreeNode> postOrder() {
        return getRoot().postOrder();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
