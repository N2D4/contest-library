package lib.collections;

import lib.trees.Tree;
import lib.trees.TreeNode;
import lib.utils.tuples.Pair;
import lib.utils.tuples.Triple;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectTree<T> extends AbstractList<T> {
    private final T[] arr;
    private final int arity;
    private final Supplier<T> supplier;
    private final BinaryOperator<T> combiner;
    private final Tree<Triple<Integer, Integer, T>> tree;

    /**
     * combiner should modify the first argument, usually by appending the second to it. The second should not be
     * changed. For immutable reduction, use the constructor taking a BinaryOperator instead
     */
    public CollectTree(Supplier<T> supplier, BiConsumer<T, T> combiner, Collection<T> from) {
        this(supplier, combiner, from, 8);
    }

    /**
     * combiner should modify the first argument, usually by appending the second to it. The second should not be
     * changed. For immutable reduction, use the constructor taking a BinaryOperator instead
     */
    public CollectTree(Supplier<T> supplier, BiConsumer<T, T> combiner, Collection<T> from, int arity) {
        // We create a stateful combiner even if the docs say we shouldn't
        this(supplier, (a, b) -> {
            combiner.accept(a, b);
            return a;
        }, from, arity);
    }

    /**
     * combiner must be stateful and pure. It may not modify either of the two arguments. For mutable reduction, use
     * the constructor taking a BiConsumer instead
     *
     * @implNote Note that in our implementation, the other constructors might call this constructor with an stateful
     * combiner. However, this is not generally supported; it only works for certain stateful combiners
     */
    public CollectTree(T identity, BinaryOperator<T> combiner, Collection<T> from) {
        this(() -> identity, combiner, from);
    }

    /**
     * combiner must be stateful and pure. It may not modify either of the two arguments. For mutable reduction, use
     * the constructor taking a BiConsumer instead
     *
     * @implNote Note that in our implementation, the other constructors might call this constructor with an stateful
     * combiner. However, this is not generally supported; it only works for certain stateful combiners
     */
    public CollectTree(T identity, BinaryOperator<T> combiner, Collection<T> from, int arity) {
        this(() -> identity, combiner, from, arity);
    }

    /**
     * combiner must be stateful and pure. It may not modify either of the two arguments. For mutable reduction, use
     * the constructor taking a BiConsumer instead
     *
     * @implNote Note that in our implementation, the other constructors might call this constructor with an stateful
     * combiner. However, this is not generally supported; it only works for certain stateful combiners
     */
    public CollectTree(Supplier<T> supplier, BinaryOperator<T> combiner, Collection<T> from) {
        this(supplier, combiner, from, 2);
    }

    /**
     * combiner must be stateful and pure. It may not modify either of the two arguments. For mutable reduction, use
     * the constructor taking a BiConsumer instead
     *
     * @implNote Note that in our implementation, the other constructors might call this constructor with an stateful
     * combiner. However, this is not generally supported; it only works for certain stateful combiners
     */
    public CollectTree(Supplier<T> supplier, BinaryOperator<T> combiner, Collection<T> from, int arity) {
        if (arity < 2) {
            throw new IllegalArgumentException("Arity must be greater than two! arity=" + arity);
        }

        this.supplier = supplier;
        this.combiner = combiner;
        this.arr = (T[]) from.toArray();
        this.arity = arity;
        this.tree = new Tree<>(null);
        createTreeNode(this.tree.getRoot(), 0, arr.length);
    }

    private void createTreeNode(TreeNode<Triple<Integer, Integer, T>> node, int start, int end) {
        if (end - start <= arity) {
            T t = supplier.get();
            for (int i = start; i < end; i++) {
                t = combiner.apply(t, arr[i]);
            }
            node.setValue(new Triple<>(start, end, t));
            return;
        }

        T t = supplier.get();
        for (int i = 0; i < arity; i++) {
            int st = start + (end - start) * i / arity;
            int en = start + (end - start) * (i + 1) / arity;
            TreeNode<Triple<Integer, Integer, T>> c = node.addChild(null);
            createTreeNode(c, st, en);
            t = combiner.apply(t, c.getValue().c);
        }

        node.setValue(new Triple<>(start, end, t));
    }

    public T collect(int start, int end) {
        return collect(tree.getRoot(), start, end, supplier.get());
    }

    private T collect(TreeNode<Triple<Integer, Integer, T>> node, int start, int end, T t) {
        Triple<Integer, Integer, T> triple = node.getValue();
        if (triple.a >= start && triple.b <= end) return combiner.apply(t, triple.c);
        if (triple.b <= start || triple.a >= end) return t;

        boolean hasChildren = false;
        for (TreeNode<Triple<Integer, Integer, T>> child : node.getChildren()) {
            hasChildren = true;
            Triple<Integer, Integer, T> ctriple = child.getValue();
            if (ctriple.a >= end) break;

            t = collect(child, start, end, t);
        }
        if (!hasChildren) {
            int m = Math.min(end, triple.b);
            for (int i = Math.max(start, triple.a); i < m; i++) {
                t = combiner.apply(t, arr[i]);
            }
        }

        return t;
    }

    @Override
    public T get(int index) {
        return arr[index];
    }

    @Override
    public T set(int index, T element) {
        T t = arr[index];
        arr[index] = element;
        return t;
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(arr).iterator();
    }

    @Override
    public int size() {
        return arr.length;
    }
}
