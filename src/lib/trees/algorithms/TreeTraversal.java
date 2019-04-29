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

    @O("n")
    public static <T> List<T> preOrder(Tree<T> tree) {
        return traverse(tree, true);
    }

    @O("n")
    public static <T> List<T> postOrder(Tree<T> tree) {
        return traverse(tree, false);
    }

    @O("n")
    private static <T> List<T> traverse(Tree<T> tree, boolean preOrder) {
        List<T> list = new ArrayList<T>();
        traverse(list, tree.getRoot(), preOrder);
        return list;
    }

    @O("n")
    private static <T> void traverse(final List<T> list, TreeNode<T> node, final boolean preOrder) {
        new IterativeRecursionIterable<TreeNode<T>, Void>(Arrays.asList(node),
                (node1, stack) -> {
                    if (preOrder) list.add(node1.getValue());
                    for (TreeNode<T> child : node1.getChildren()) {
                        stack.accept(child);
                    }
                    return node1.getValue();
                },
                t -> {
                    if (!preOrder) list.add(t);
                    return null;
                }
        ).runAll();
    }

}
