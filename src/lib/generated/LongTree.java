/* Generated by Generificator on Tue Dec 03 22:32:58 CET 2019 from Tree.java */

package lib.generated;

import java.util.function.*;
import lib.generated.*;
import lib.trees.*;

import lib.algorithms.O;
import lib.utils.tuples.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.function.DoubleBinaryOperator;


public class LongTree implements Serializable {
    private LongTreeNode root;
    final Pair<Double, DoubleBinaryOperator> distanceFolder;

    public LongTree(long rootValue) {
        this(rootValue, null);
    }

    public LongTree(long rootValue, Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this(distanceFolder);
        initRoot(new LongTreeNode(rootValue, this));
    }

    protected LongTree() {
        this((Pair<Double, DoubleBinaryOperator>) null);
    }

    protected LongTree(Pair<Double, DoubleBinaryOperator> distanceFolder) {
        this.distanceFolder = distanceFolder == null ? new Pair<Double, DoubleBinaryOperator>(0.0, Double::sum) : distanceFolder;
    }

    protected void initRoot(LongTreeNode node) {
        if (root != null) throw new IllegalStateException("Can't modify the root after it has already been initialized!");
        this.root = node;
    }

    public LongTreeNode getRoot() {
        return root;
    }



    @O("n")
    public List<LongTreeNode> leafNodes() {
        return getRoot().leafNodes();
    }

    @O("n")
    public List<LongTreeNode> preOrder() {
        return getRoot().preOrder();
    }

    @O("n")
    public List<LongTreeNode> postOrder() {
        return getRoot().postOrder();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
