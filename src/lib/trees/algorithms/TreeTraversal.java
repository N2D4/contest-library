package lib.trees.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.trees.Tree;
import lib.trees.TreeNode;
import lib.utils.QueueUtils;
import lib.utils.Utils;
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
    public static <T> List<TreeNode<T>> leafNodes(Tree<T> tree) {
        return leafNodes(tree.getRoot());
    }

    @O("n")
    public static <T> List<TreeNode<T>> preOrder(Tree<T> tree) {
        return preOrder(tree.getRoot());
    }

    @O("n")
    public static <T> List<TreeNode<T>> postOrder(Tree<T> tree) {
        return postOrder(tree.getRoot());
    }

    @O("n")
    public static <T> List<TreeNode<T>> leafNodes(TreeNode<T> root) {
        return traverse(root, 2);
    }

    @O("n")
    public static <T> List<TreeNode<T>> preOrder(TreeNode<T> root) {
        return traverse(root, 0);
    }

    @O("n")
    public static <T> List<TreeNode<T>> postOrder(TreeNode<T> root) {
        return traverse(root, 1);
    }


    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private static <T> List<TreeNode<T>> traverse(TreeNode<T> root, int mode) {
        List<TreeNode<T>> list = new ArrayList<>();
        traverse(list, root, mode);
        return list;
    }

    /**
     * Mode 0 = pre-order, mode 1 = post-order, mode 2 = leaf nodes
     */
    @O("n")
    private static <T> void traverse(List<TreeNode<T>> list, TreeNode<T> node, int mode) {
        Queue<TreeNode<T>> queue = QueueUtils.createLIFO();
        queue.add(node);

        while (!queue.isEmpty()) {
            TreeNode<T> n = queue.remove();
            if (mode != 2 || n.getChildCount() == 0) list.add(n);

            Iterable<TreeNode<T>> children = mode == 1 ? n.getChildren() : Utils.reverseIterable(n.getChildren());
            for (TreeNode<T> child : children) {
                queue.add(child);
            }
        }

        if (mode == 1) Collections.reverse(list);

        /*
        if (depthRemaining > 0) {
            // let's do a recursive call, which is often faster

            if (mode == 0 || (mode == 2 && node.getChildCount() == 0)) list.add(node);
            for (TreeNode<T> child : node.getChildren()) {
                traverse(list, child, mode, depthRemaining - 1);
            }
            if (mode == 1) list.add(node);

        } else {
            // use an iterative algorithm

            new IterativeRecursionIterable<TreeNode<T>, Void>(Arrays.asList(node),
                    (node1, stack) -> {
                        if (mode == 0 || (mode == 2 && node1.getChildCount() == 0)) list.add(node1);
                        for (TreeNode<T> child : node1.getChildren()) {
                            stack.accept(child);
                        }
                        return node1;
                    },
                    t -> {
                        if (mode == 1) list.add(t);
                        return null;
                    }
            ).runAll();
        }*/
    }

}
