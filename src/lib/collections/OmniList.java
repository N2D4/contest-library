package lib.collections;

import lib.utils.ArrayUtils;
import lib.utils.Utils;
import lib.utils.various.Range;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OmniList<T> extends AbstractList<T> implements RandomAccess {
    // Please note the terminology difference between "begin-end" and "first-last"; the former is inclusive-exclusive,
    // while the latter is inclusive-inclusive.

    private Node root;
    private Node first;
    private Node last;

    /**
     * An array-based list that can be modified on both ends in constant time.
     */
    private static class DoubleEndedArrayList<E> {
        final int DEFAULT_CAPACITY = 16;

        private Object[] arr;
        private int capMask;
        private int c_begin;
        private int c_end;

        public DoubleEndedArrayList() {
            arr = new Object[DEFAULT_CAPACITY];
            capMask = arr.length - 1;
            c_begin = 0;
            c_end = 0;
        }

        public int begin() {
            return c_begin;
        }

        public int end() {
            return c_end;
        }

        public int size() {
            return end() - begin();
        }

        public Range range() {
            return new Range(begin(), end());
        }

        /**
         * Doubles capacity
         */
        private void doubleCapacity() {
            Object[] oldArr = arr;
            int oldCapMask = capMask;
            arr = new Object[arr.length << 1];
            capMask = arr.length - 1;

            int beg = begin();
            int end = end();

            ArrayUtils.verboseCopy(
                    oldArr, beg & oldCapMask, end & oldCapMask,
                    arr, beg & capMask
            );
        }

        private void ensureCapacity(int capacity) {
            while (capacity > arr.length) {
                doubleCapacity();
            }
        }

        public E get(int index) {
            rangeCheck(index);
            return (E) arr[index & capMask];
        }

        public E set(int index, E e) {
            E r = get(index);
            arr[index & capMask] = e;
            return r;
        }

        public void addFirst(E e) {
            ensureCapacity(size() + 1);
            arr[--c_begin & capMask] = e;
        }

        public void addLast(E e) {
            ensureCapacity(size() + 1);
            arr[c_end++ & capMask] = e;
        }

        public E removeFirst() {
            if (size() <= 0) throw new IllegalArgumentException("This container is empty!");
            return (E) arr[c_begin++ & capMask];
        }

        public E removeLast() {
            if (size() <= 0) throw new IllegalArgumentException("This container is empty!");
            return (E) arr[--c_end & capMask];
        }

        public void addFromBegin(int index, E e) {
            addFirst(get(begin()));
            for (int i = begin() + 1; i < index; i++) {
                set(i, get(i+1));
            }
            set(index, e);
        }

        public void addFromEnd(int index, E e) {
            addLast(get(end()));
            for (int i = end() - 2; i > index; i--) {
                set(i, get(i-1));
            }
            set(index, e);
        }

        public E removeFromBegin(int index) {
            E r = get(index);
            int to = begin();
            for (int i = index; i > to; i--) {
                set(i, get(i-1));
            }
            removeFirst();
            return r;
        }

        public E removeFromEnd(int index) {
            E r = get(index);
            int to = end() - 1;
            for (int i = index; i < to; i++) {
                set(i, get(i+1));
            }
            removeLast();
            return r;
        }


        private void rangeCheck(int index) {
            if (!new Range(begin(), end()).contains(index)) throw new IllegalArgumentException("Index " + index + " not in range [" + begin() + ", " + end() + ")!");
        }

        private void rangeCheckAdd(int index) {
            if (!new Range(begin(), end() + 1).contains(index)) throw new IllegalArgumentException("Index " + index + " not in range [" + begin() + ", " + end() + "]!");
        }
    }

    /**
     * A view of a range over an array.
     */
    private class ArrView {
        private final DoubleEndedArrayList<T> arr;
        private int start;
        private int end;

        public ArrView(DoubleEndedArrayList<T> arr, int start, int end) {
            if (start < arr.begin() || end > arr.end()) throw new IllegalArgumentException("start: " + start + ", end: " + end + ", arr.range(): " + arr.range());
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        public ArrView(ArrView arr, int start, int end) {
            this(arr.arr, arr.start + start, arr.start + end);
            if (start < 0 || end > arr.size()) throw new IllegalArgumentException("start: " + start + ", end: " + end + ", arr.size(): " + arr.size());
        }

        public int size() {
            return end - start;
        }

        public T get(int index) {
            rangeCheck(index);
            return arr.get(index + start);
        }

        public T set(int index, T t) {
            rangeCheck(index);
            return arr.set(index + start, t);
        }

        public void add(int index, T t) {
            rangeCheckAdd(index);
            if (isLeftExtendible()) {
                arr.addFromBegin(index + start, t);
                start--;
            } else if (isRightExtendible()) {
                arr.addFromEnd(index + start, t);
                end++;
            } else {
                throw new IllegalArgumentException("This instance of ArrView can't be extended!");
            }
        }

        public T remove(int index) {
            rangeCheck(index);
            if (isLeftExtendible()) {
                T t = arr.removeFromBegin(index + start);
                start++;
                return t;
            } else if (isRightExtendible()) {
                T t = arr.removeFromEnd(index + start);
                end--;
                return t;
            } else {
                // TODO consider whether we maybe wanna still remove, by just shrinking the view (could cause memory leaks)
                //   EDIT: yes we want to do that (we're doing it in shortenLeft/Right, do it here too)
                throw new IllegalArgumentException("This instance of ArrView isn't extendible on both sides! (unimplemented)");
            }
        }

        public boolean isLeftExtendible() {
            return start == arr.begin();
        }

        public boolean isRightExtendible() {
            return end == arr.end();
        }

        public void extendLeft(T t) {
            if (!isLeftExtendible()) throw new IllegalArgumentException();
            this.arr.addFirst(t);
            this.start--;
        }

        public void extendRight(T t) {
            if (!isRightExtendible()) throw new IllegalArgumentException();
            this.arr.addLast(t);
            this.end++;
        }

        public T shortenLeft() {
            T t = this.arr.get(this.arr.begin());
            if (isLeftExtendible()) {
                this.arr.removeFirst();
            }
            // TODO slow (memory leak; if not left extendible, the view size will shrink and the array be kept in memory, but the elements at that index will never be used again)
            this.start++;
            return t;
        }

        public T shortenRight() {
            T t = this.arr.get(this.arr.end() - 1);
            if (isRightExtendible()) {
                this.arr.removeLast();
            }
            // TODO slow (memory leak; if not right extendible, the view size will shrink and the array be kept in memory, but the elements at that index will never be used again)
            this.end--;
            return t;
        }

        private void rangeCheck(int index) {
            if (!new Range(0, size()).contains(index)) throw new IllegalArgumentException("Index " + index + " not in range [0, " + size() + ")!");
        }

        private void rangeCheckAdd(int index) {
            if (!new Range(0, size() + 1).contains(index)) throw new IllegalArgumentException("Index " + index + " not in range [0, " + size() + "]!");
        }

        @Override
        public String toString() {
            return "[" + IntStream.range(start, end).mapToObj(a -> "" + arr.get(a)).collect(Collectors.joining(", ")) + "]";
        }

    }

    private class Node {
        private Node parent;
        /**
         * If arr == null (non-leaf node), left and right are the left and right child nodes (in which case they will
         * always be non-null). If arr != null (leaf node), left and right are the previous and next nodes in the linked
         * list.
         */
        private Node left;
        private Node right;

        /**
         * arr is the array of elements in this node, if this node is a leaf node, or null if there are no elements in
         * this array. May never be empty except if this leaf node is also the root node.
         */
        private ArrView arr;

        /**
         * Only used when this is a non-leaf node.
         */
        private int totalLengthCached = -1;

        /**
         * Creates a new root node.
         */
        public Node() {
            this(null, new ArrView(new DoubleEndedArrayList<>(), 0, 0));
        }

        /**
         * Should eventually be followed up with a call to updateTotalLength()
         */
        private Node(Node parent, ArrView view) {
            this.parent = parent;
            this.arr = view;
            this.left = null;
            this.right = null;
        }

        public Ref ref(int index) {
            if (arr != null) return new Ref(this, index);

            if (index < left.totalLength()) return left.ref(index);
            else return right.ref(index - left.totalLength());
        }

        public Ref first() {
            return ref(0);
        }

        public Ref last() {
            return ref(totalLength() - 1);
        }

        public int totalLength() {
            return isLeafNode() ? arr.size() : totalLengthCached;
        }

        public boolean isLeafNode() {
            return arr != null;
        }

        /**
         * Splits this leaf node into three leaf nodes; arr[0:index], a newly allocated array view extendible on both
         * sides containing the given new elements, and arr[index:arr.length]. (In total, this will cause three new leaf
         * nodes and one new non-leaf node to be created, and this node will be re-purposed as a non-leaf node.)
         */
        void addBySplit(int index, T... newElements) {
            // TODO slow (rotations are missing)
            if (!isLeafNode()) throw new IllegalArgumentException("Must be a leaf node!");
            if (newElements.length <= 0) throw new IllegalArgumentException("Need at least one element to add!");

            ArrView newArr = new ArrView(new DoubleEndedArrayList<>(), 0, 0);
            for (T t : newElements) {
                newArr.extendRight(t);
            }

            Node newLeft = new Node(this, null);
            Node newLeftLeft = new Node(newLeft, new ArrView(arr, 0, index));
            Node newLeftRight = new Node(newLeft, newArr);
            Node newRight = new Node(this, new ArrView(arr, index, arr.size()));


            newLeftLeft.left = this.left;
            newLeftLeft.right = newLeftRight;

            newLeftRight.left = newLeftLeft;
            newLeftRight.right = newRight;

            newLeft.left = newLeftLeft;
            newLeft.right = newLeftRight;

            newRight.left = newLeftRight;
            newRight.right = this.right;

            this.arr = null;
            if (this.left == null) {
                OmniList.this.first = newLeftLeft;
            } else {
                this.left.right = newLeftLeft;
            }
            if (this.right == null) {
                OmniList.this.last = newRight;
            } else {
                this.right.left = newRight;
            }
            this.left = newLeft;
            this.right = newRight;


            newLeftLeft.updateTotalLength();
            newLeftRight.updateTotalLength();
            newRight.updateTotalLength();
        }

        void addFirst(T t) {
            if (!isLeafNode()) throw new IllegalArgumentException("Must be a leaf node!");
            if (arr.isLeftExtendible()) {
                arr.extendLeft(t);
                updateTotalLength();
            } else {
                left.addLast(t);
            }
        }

        void addLast(T t) {
            if (arr.isRightExtendible()) {
                if (!isLeafNode()) throw new IllegalArgumentException("Must be a leaf node!");
                arr.extendRight(t);
                updateTotalLength();
            } else {
                right.addFirst(t);
            }
        }

        T remove(int index) {
            if (!(new Range(0, this.totalLength()).contains(index))) throw new IllegalArgumentException("Index " + index + " not in range [0, " + this.totalLength() + ")!");

            T t;
            if (this.totalLength() <= 1) {
                // nuke this node
                // TODO unimplemented
                throw new RuntimeException();
            } else if (index == 0) {
                t = this.arr.shortenLeft();
            } else if (index == this.totalLength() - 1) {
                t = this.arr.shortenRight();
            } else {
                // TODO unimplemented
                throw new RuntimeException();
            }
            updateTotalLength();
            return t;
        }

        private void updateTotalLength() {
            // TODO slow (implement lazy updating; don't update until queried)
            if (!isLeafNode()) this.totalLengthCached = left.totalLength() + right.totalLength();
            if (parent != null) parent.updateTotalLength();
        }

        public boolean isConsistent() {
            if (!isLeafNode()) {
                return left != null && right != null
                        && left.isConsistent() && right.isConsistent()
                        && totalLength() == left.totalLength() + right.totalLength()
                        && left.totalLength() > 0 && right.totalLength() > 0
                        && left.parent == this && right.parent == this;
            } else {
                return totalLength() == arr.size()
                        && (left == null ? OmniList.this.first == this : left.right == this)
                        && (right == null ? OmniList.this.last == this : right.left == this)
                        && (this.arr.isLeftExtendible() || this.left.arr.isRightExtendible())
                        && (this.arr.isRightExtendible() || this.right.arr.isLeftExtendible());
            }
        }

        public String toString() {
            String s;
            if (isLeafNode()) {
                s = arr.toString();
            } else {
                s = "{" + left + ", " + right + "}";
            }
            if (isConsistent()) {
                return s;
            } else {
                return "!!" + s;
            }
        }
    }

    /**
     * A ref is a reference to a particular element in the omni list. It is uniquely determined by a reference to the
     * leaf node which contains the element, and an integer index which determines the index of the element inside that
     * leaf node. Methods like prev() or next() return a new reference, instead of modifying this one. (It is not an
     * iterator. Also note that each Ref points to an element, whereas iterators point to positions between elements)
     */
    private class Ref {
        private Node node;
        private int index;

        Ref(Node node, int index) {
            if (!(new Range(0, node.totalLength()).contains(index))) throw new IllegalArgumentException("Index " + index + " not in range [0, " + node.totalLength() + ")!");
            this.node = node;
            this.index = index;
        }

        private OmniList<T> getEnclosingOmniListInstance() {
            // Can someone tell me whether there's a way to get the enclosing instance of an object that's not `this`?
            return OmniList.this;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof OmniList<?>.Ref)) return false;
            OmniList<?>.Ref ref = (OmniList<?>.Ref) other;
            if (this.getEnclosingOmniListInstance() != ref.getEnclosingOmniListInstance()) return false;
            return Utils.equals(ref.node, this.node) && Utils.equals(ref.index, this.index);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, node);
        }

        public T get() {
            return node.arr.get(index);
        }

        public T set(T t) {
            return node.arr.set(index, t);
        }

        public boolean hasPrevious() {
            return index >= 1 || node.left != null;
        }

        public boolean hasNext() {
            return index < node.totalLength() - 1 || node.right != null;
        }

        public Ref next() {
            if (!hasNext()) throw new NoSuchElementException();
            if (index >= node.totalLength() - 1) {
                return node.right.first();
            } else {
                return new Ref(node, index + 1);
            }
        }

        public Ref prev() {
            if (!hasPrevious()) throw new NoSuchElementException();
            if (index <= 0) {
                return node.left.last();
            } else {
                return new Ref(node, index - 1);
            }
        }

        public Ref addBefore(T t) {
            T oldTForAssertion = get();
            if (index <= 0) {
                node.addFirst(t);
                index++;
            } else {
                node.addBySplit(index, t);
                Ref newRef = node.ref(index + 1);
                this.node = newRef.node;
                this.index = newRef.index;
            }
            assert oldTForAssertion == get();
            return prev();
        }

        public Ref addAfter(T t) {
            T oldTForAssertion = get();
            if (index >= node.totalLength() - 1) {
                node.addLast(t);
            } else {
                next().addBefore(t);
            }
            assert oldTForAssertion == get();
            return next();
        }

        /**
         * Calling this method invalidates this Ref object.
         */
        public T remove() {
            T t = node.remove(index);
            invalidate();
            return t;
        }

        private void invalidate() {
            // TODO there should be a better way to invalidate than just setting all members to null...
            node = null;
            index = -1;
        }

        public boolean isConsistent() {
            return node != null
                    && node.isLeafNode()
                    && new Range(0, node.totalLength()).contains(index)
                    && node.isConsistent();
        }
    }











    public OmniList() {
        clear();
    }

    private Ref ref(int index) {
        if (index == 0) return first.first();
        if (index == size() - 1) return last.last();
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
        return root.totalLength();
    }

    @Override
    public void add(int index, T element) {
        if (index == size()) last.addLast(element);
        else ref(index).addBefore(element);
    }

    @Override
    public T remove(int index) {
        return ref(index).remove();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {
            Ref ref = index == size() ? null : ref(index);
            Ref last = null;
            int indx = index;

            @Override
            public boolean hasNext() {
                return ref != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                last = ref;
                if (ref.hasNext()) {
                    ref = ref.next();
                } else {
                    ref = null;
                }
                indx++;
                return last.get();
            }

            @Override
            public boolean hasPrevious() {
                return ref == null ? size() >= 1 : ref.hasPrevious();
            }

            @Override
            public T previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                if (ref == null) {
                    ref = ref(size() - 1);
                } else {
                    ref = ref.prev();
                }
                indx--;
                return (last = ref).get();
            }

            @Override
            public int nextIndex() {
                next();
                return indx - 1;
            }

            @Override
            public int previousIndex() {
                previous();
                return indx;
            }

            @Override
            public void remove() {
                if (last == null) throw new NoSuchElementException();
                last.remove();
            }

            @Override
            public void set(T t) {
                if (last == null) throw new NoSuchElementException();
                last.set(t);
            }

            @Override
            public void add(T t) {
                if (last == null) throw new NoSuchElementException();
                if (ref == null) {
                    add(t);
                } else {
                    ref.addBefore(t);
                }
            }
        };
    }

    @Override
    public int indexOf(Object o) {
        // TODO slow
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO slow
        return super.lastIndexOf(o);
    }

    @Override
    public void clear() {
        this.first = this.last = this.root = new Node();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1; // TODO slow
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

    public boolean isConsistent() {
        return this.root.isConsistent();
    }

    public String treeToString() {
        return root.toString();
    }
}

