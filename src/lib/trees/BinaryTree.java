package lib.trees;

public class BinaryTree<T> extends Tree<T> {
    public BinaryTree(T rootValue) {
        super();
    }

    protected BinaryTree(BinaryTreeNode<T> rootNode) {
        super();
        initRoot(rootNode);
    }
}
