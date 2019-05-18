package lib.trees.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.trees.Tree;
import lib.trees.TreeNode;
import lib.utils.various.IterativeRecursionIterable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class TreeTraversal extends Algorithm {

    private TreeTraversal() {
        // Quite dusty here...
    }

    public static <T> List<T> leafNodes(Tree<T> tree) {
        return traverse(tree, 2);
    }

    @O("n")
    public static <T> List<T> preOrder(Tree<T> tree) {
        return traverse(tree, 0);
    }

    @O("n")
    public static <T> List<T> postOrder(Tree<T> tree) {
        return traverse(tree, 1);
    }


    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private static <T> List<T> traverse(Tree<T> tree, int mode) {
        List<T> list = new ArrayList<T>();
        traverse(list, tree.getRoot(), mode);
        return list;
    }

    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private static <T> void traverse(final List<T> list, TreeNode<T> node, final int mode) {
        new IterativeRecursionIterable<TreeNode<T>, Void>(Arrays.asList(node),
                (node1, stack) -> {
                    if (mode == 0 || (mode == 2 && node1.getChildCount() == 0)) list.add(node1.getValue());
                    for (TreeNode<T> child : node1.getChildren()) {
                        stack.accept(child);
                    }
                    return node1.getValue();
                },
                t -> {
                    if (mode == 1) list.add(t);
                    return null;
                }
        ).runAll();
    }

}
