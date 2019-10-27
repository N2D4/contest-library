package lib.collections;

import java.util.*;

public class OmniList<T> extends AbstractList<T> {

    public OmniList() {
        this.first = this.last = this.root = new Node();
    }

    private Node root;
    private Node first;
    private Node last;

    /**
     * An array-based list that can be modified on both ends in constant time.
     */
    private class DoubleEndedArrayList<T> extends ArrayList<T> {
        // TODO slow (shouldn't extend array list - currently only has const time on the right end)
    }

    /**
     * A view of a range over an array.
     */
    private class ArrView extends AbstractList<T> {
        private final ArrayList<T> arr;
        private int start;
        private int end;

        public ArrView() {
            this.arr = new ArrayList<>();
            start = 0;
            end = 0;
        }

        public ArrView(ArrayList<T> arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        public int size() {
            return end - start;
        }

        @Override
        public T get(int index) {
            return arr.get(index + start);
        }

        public boolean isLeftExtendible() {
            return start == 0;
        }

        public boolean isRightExtendible() {
            return end == arr.size();
        }

        public void extendLeft(T t) {
            if (!isLeftExtendible()) throw new IllegalArgumentException();
            this.arr.add(0, t);
            this.end++;
        }

        public void extendRight(T t) {
            if (!isRightExtendible()) throw new IllegalArgumentException();
            this.arr.add(t);
            this.end++;
        }
    }

    private class Node {
        /**
         * If arr == null (non-leaf node), left and right are the left and right child nodes. If arr != null (leaf
         * node), left and right are the previous and next nodes in the linked list
         */
        private Node left = null;
        private Node right = null;

        /**
         * totalLength is the number of elements in this node, including elements in its child nodes. If this is a leaf
         * node
         */
        private int totalLength = 0;

        /**
         * arr is the array of elements in this node, if this node is a leaf node.
         */
        ArrView arr = new ArrView();

        public Ref ref(int index) {
            if (arr != null) return new Ref(this, index);

            if (index < left.totalLength) return left.ref(index);
            else return right.ref(index - left.totalLength);
        }

        public Ref begin() {
            return ref(0);
        }

        public Ref end() {
            return ref(totalLength - 1);
        }
    }

    /**
     * A ref is a reference to a particular element in the omni list. It is uniquely determined by a reference to the
     * leaf node which contains the element, and an integer index which determines the index of the element inside that
     * leaf node.
     */
    private class Ref {
        private Node node;
        private int index;

        Ref(Node node, int index) {
            this.node = node;
            this.index = index;
        }

        public T get() {
            return node.arr.get(index);
        }

        public T set(T t) {
            return node.arr.set(index, t);
        }

        public boolean hasPrevious() {
            return index >= 1;
        }

        public boolean hasNext() {
            return index < node.totalLength - 1;
        }

        public Ref next() {
            if (index == node.totalLength - 1) {
                if (node.left == null) {
                    return null;
                } else {
                    return node.left.begin();
                }
            }
            return new Ref(node, index + 1);
        }

        public Ref prev() {
            if (index == 0) {
                if (node.left == null) {
                    return null;
                } else {
                    return node.left.end();
                }
            }
            return new Ref(node, index - 1);
        }

        public void addBefore(T t) {
            if (index <= 0) {
                node.arr.add(index, t);
            }
            node.arr.add(index, t); // TODO slow
            index++;
        }

        public void addAfter(T t) {
            if (index >= node.totalLength - 1) {
                node.arr.add(index + 1, t);
            }
            node.arr.add(index + 1, t); // TODO slow
        }

        public T remove() {
            return node.arr.remove(index); // TODO slow
        }
    }

    private Ref ref(int index) {
        return root.ref(index);
    }

    @Override
    public T get(int index) {
        return ref(index).get();
    }

    @Override
    public T set(int index, T element) {
        return ref(index).set(element);
    }

    @Override
    public int size() {
        return root.totalLength;
    }

    @Override
    public void add(int index, T element) {
        if (index == size()) last.end().addAfter(element);
        else ref(index).addBefore(element);
    }

    @Override
    public T remove(int index) {
        return ref(index).remove();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public void clear() {

    }












    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator(0);
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            remove(indexOf(o));
            return true;
        }
        return false;
    }
}
