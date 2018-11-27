package lib.trees;

public class BinaryTreeNode<T> extends TreeNode<T> {

    public BinaryTreeNode(T value) {
        super(value);
    }

    protected BinaryTreeNode(T value, Tree<T> tree) {
        super(value, tree);
    }

    protected BinaryTreeNode(T value, TreeNode<T> parent, double distanceToParent) {
        super(value, parent, distanceToParent);
    }
}
