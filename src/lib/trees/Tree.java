package lib.trees;

import java.io.Serializable;

public class Tree<T> implements Serializable {
    private TreeNode<T> root;

    public Tree(T rootValue) {
        initRoot(new TreeNode(rootValue, this));
    }

    protected Tree() {
        // Quite dusty here...
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
