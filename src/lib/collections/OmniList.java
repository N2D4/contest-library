package lib.collections;

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
    private class DoubleEndedArrayList<T> extends ArrayList<T> {
        // TODO slow (shouldn't extend array list - currently only has const time on the right end)
    }

    /**
     * A view of a range over an array.
     */
    private class ArrView {
        private final DoubleEndedArrayList<T> arr;
        private int start;
        private int end;

        public ArrView() {
            this.arr = new DoubleEndedArrayList<>();
            start = 0;
            end = 0;
        }

        public ArrView(DoubleEndedArrayList<T> arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
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
            end++;
            arr.add(index + start, t);
        }

        public T remove(int index) {
            rangeCheck(index);
            end--;
            return arr.remove(index + start);
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

        private void rangeCheck(int index) {
            if (!new Range(0, size()).contains(index)) throw new IllegalArgumentException("Index " + index + " not in range [0, " + size() + ")!");
        }

        private void rangeCheckAdd(int index) {
            if (!new Range(0, size() + 1).contains(index)) throw new IllegalArgumentException("Index " + index + " not in range [0, " + size() + ")!");
        }

        @Override
        public String toString() {
            return "[" + IntStream.range(start, end).mapToObj(a -> "" + arr.get(a)).collect(Collectors.joining(", ")) + "]";
        }

    }

    private class Node {
        /**
         * If arr == null (non-leaf node), left and right are the left and right child nodes (in which case they will
         * always be non-null). If arr != null (leaf node), left and right are the previous and next nodes in the linked
         * list.
         */
        private Node left;
        private Node right;

        /**
         * arr is the array of elements in this node, if this node is a leaf node.
         */
        ArrView arr;

        /**
         * Creates a new leaf node.
         */
        public Node() {
            left = null;
            right = null;
            arr = new ArrView();
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
            // TODO slow
            return arr == null ? left.totalLength() + right.totalLength() : arr.size();
        }

        public boolean isConsistent() {
            if (arr == null) {
                return left.isConsistent() && right.isConsistent()
                        && totalLength() == left.totalLength() + right.totalLength()
                        && left.totalLength() > 0 && right.totalLength() > 0;
            } else {
                return totalLength() == arr.size();
            }
        }
    }

    /**
     * A ref is a reference to a particular element in the omni list. It is uniquely determined by a reference to the
     * leaf node which contains the element, and an integer index which determines the index of the element inside that
     * leaf node. Methods like prev() or next() return a new reference, instead of modifying this one. (It is not an
     * iterator.)
     */
    private class Ref {
        private Node node;
        private int index;

        Ref(Node node, int index) {
            this.node = node;
            this.index = index;
        }

        private OmniList<T> getEnclosingOmniListInstance() {
            // Can someone tell me whether there's a way to get the enclosing instance of an object that's not `this`?
            return OmniList.this;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof OmniList.Ref)) return false;
            Ref ref = (OmniList.Ref) other;
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

        public void addBefore(T t) {
            if (index <= 0) {
                node.arr.add(index, t);
            } else {
                node.arr.add(index, t); // TODO slow
            }
            index++;
        }

        public void addAfter(T t) {
            if (index >= node.totalLength() - 1) {
                node.arr.add(index + 1, t);
            } else {
                node.arr.add(index + 1, t); // TODO slow
            }
        }

        public T remove() {
            return node.arr.remove(index); // TODO slow
            // TODO node should never be empty, if it's empty, move it around
        }

        public boolean isConsistent() {
            return node != null
                    && node.arr != null
                    && new Range(0, node.totalLength()).contains(index)
                    && node.isConsistent();
        }
    }











    public OmniList() {
        clear();
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
        return root.totalLength();
    }

    @Override
    public void add(int index, T element) {
        if (index == size()) last.last().addAfter(element);
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
}

