package lib.collections;

import lib.algorithms.O;
import lib.utils.Utils;
import lib.utils.tuples.Pair;
import lib.utils.various.Range;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class PartitionList<T> extends AbstractList<T> implements RandomAccess, Serializable {
    private int size;
    private NavigableMap<Integer, T> map = new TreeMap<Integer, T>();


    public PartitionList() {
        this(0);
    }

    public PartitionList(int initialSize) {
        this(initialSize, null);
    }

    public PartitionList(int initialSize, T value) {
        this.size = initialSize;
        if (initialSize > 0) map.put(0, value);
    }


    public void resize(int size) {
        if (size < 0) throw new IllegalArgumentException("New size must be positive! (is: " + size + ")");

        modCount++;

        int oldSize = this.size;
        this.size = size;

        if (size < oldSize) {
            map.tailMap(size).clear();
        }
        if (oldSize <= 0 && size > oldSize) {
            map.put(0, null);
        }
    }

    @Override
    public T get(int index) {
        rangeCheck(index);

        return map.floorEntry(index).getValue();
    }

    @Override
    public int size() {
        return size;
    }






    @Override
    public T set(int index, T element) {
        T prev = get(index);
        if (prev == element) return prev;

        setRange(new Range(index, index + 1), element);
        return prev;
    }

    public void setRange(Range range, T element) {
        rangeCheck(range);
        if (range.b <= range.a) return;
        modCount++;


        if (range.b < size) map.put(range.b, get(range.b));
        map.subMap(range.a, range.b).clear();
        if (range.a <= 0 || get(range.a) != element) {
            Map.Entry<Integer, T> ceil = map.ceilingEntry(range.a);
            if (ceil != null && ceil.getValue() == element) {
                map.remove(ceil.getKey());
            }
            map.put(range.a, element);
        }
    }

    public void setAndResize(Range range, T element) {
        if (range.b < range.a) throw new IllegalArgumentException("Range index b must be greater or equal to a!");
        if (range.b > size()) resize(range.b);

        setRange(range, element);
    }





    @Override
    public void add(int index, T element) {
        addAll(index, 1, element);
    }

    public boolean addAll(int index, int count, T element) {
        if (count < 0) throw new IllegalArgumentException("Count must be non-negative! (is: " + count + ")");
        if (count == 0) return false;

        modCount++;

        resize(size() + count);
        for (Map.Entry<Integer, T> entry : new ArrayList<>(map.tailMap(index, true).descendingMap().entrySet())) {
            int npos = entry.getKey() + count;
            if (npos >= size()) continue;
            T val = entry.getValue();

            map.remove(entry.getKey());
            map.put(npos, val);
        }

        setRange(new Range(index, index + count), element);

        return true;
    }

    public boolean addAll(int count, T element) {
        return addAll(size(), count, element);
    }

    public boolean addRange(Range range, T element) {
        return addAll(range.a, range.size(), element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> list) {
        if (!(list instanceof PartitionList)) return super.addAll(index, list);

        upperRangeCheck(index);
        if (list.size() <= 0) return false;

        PartitionList<? extends T> plist = (PartitionList) list;
        for (Iterator<? extends Pair<Range, ? extends T>> iterator = plist.partitionsIterator(true); iterator.hasNext();) {
            Pair<Range, ? extends T> partition = iterator.next();
            Range range = partition.a;
            this.addAll(index + range.a, range.size(), partition.b);
        }

        return true;
    }




    @Override
    public T remove(int index) {
        T res = get(index);
        removeRange(new Range(index, index + 1));
        return res;
    }

    @Override
    protected void removeRange(int a, int b) {
        removeRange(new Range(a, b));
    }

    public void removeRange(Range range) {
        rangeCheck(range);
        if (range.size() <= 0) return;
        modCount++;

        T lastB = get(range.b);
        map.subMap(range.a, range.b).clear();
        if (lastB != get(range.b)) map.putIfAbsent(range.b, lastB);
        boolean isFirst = true;
        for (Map.Entry<Integer, T> entry : new ArrayList<Map.Entry<Integer, T>>(map.tailMap(range.b, true).entrySet())) {
            int npos = entry.getKey() - range.size();
            T val = entry.getValue();

            map.remove(entry.getKey());
            if (!isFirst || get(npos) != val) map.put(npos, val);
            isFirst = false;
        }
        resize(size() - range.size());
    }



    /**
     * Same as #applyAll(...)
     */
    public void mapAll(Function<T, T> mapper) {
        mapRange(new Range(0, size()), mapper);
    }

    /**
     * Same as #applyRange(...)
     */
    public void mapRange(Range range, Function <T, T> mapper) {
        for (PartitionIterator<T> iterator = partitionsIterator(range); iterator.hasNext();) {
            Pair<Range, T> partition = iterator.next();
            iterator.set(mapper.apply(partition.b));
        }
    }

    /**
     * Same as #mapAll(...)
     */
    public void applyAll(Function<T, T> mapper) {
        mapAll(mapper);
    }

    /**
     * Same as #mapRange(...)
     */
    public void applyRange(Range range, Function <T, T> mapper) {
        mapRange(range, mapper);
    }





    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PartitionList)) return super.equals(obj);

        PartitionList<T> other = (PartitionList<T>) obj;
        if (this.partitionCount() != other.partitionCount()) return false;
        return this.partitions().equals(other.partitions());
    }

    @Override
    public int hashCode() {
        // TODO This works, but is slow (loops over every element)
        return super.hashCode();
    }





    private Range rangeOf(Object element, boolean descending) {
        for (Iterator<Pair<Range, T>> iterator = partitionsIterator(descending); iterator.hasNext();) {
            Pair<Range, T> entry = iterator.next();
            if (Objects.equals(entry.getValue(), element)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Range rangeOf(Object element) {
        return rangeOf(element, false);
    }

    public Range lastRangeOf(Object element) {
        return rangeOf(element, true);
    }

    @Override
    public int indexOf(Object element) {
        return rangeOf(element).a;
    }

    @Override
    public int lastIndexOf(Object element) {
        return lastRangeOf(element).b - 1;
    }


    @O("n")
    public int getPartitionIndexOf(int elementIndex) {
        rangeCheck(elementIndex);
        return map.headMap(elementIndex, false).size();
    }

    public List<Pair<Range, T>> partitions() {
        return partitions(false);
    }

    protected List<Pair<Range, T>> partitions(boolean descending) {
        return Collections.unmodifiableList(Utils.toArrayList(partitionsIterator(descending), partitionCount()));
    }

    public int partitionCount() {
        return map.size();
    }

    public PartitionIterator<T> partitionsIterator() {
        return partitionsIterator(false);
    }

    protected PartitionIterator<T> partitionsIterator(boolean descending) {
        return partitionsIterator(new Range(0, size()), descending);
    }

    public PartitionIterator<T> partitionsIterator(Range range) {
        return partitionsIterator(range, false);
    }

    protected PartitionIterator<T> partitionsIterator(Range range, boolean descending) {
        return new PartitionItr(range, descending);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListItr(index);
    }

    private abstract class Itr<E> implements Iterator<E> {
        private int expectedModCount = modCount;

        protected Itr() {
            updateModCount();
        }

        protected void checkModCount() {
            if (expectedModCount != modCount) throw new ConcurrentModificationException();
        }

        protected void updateModCount() {
            expectedModCount = modCount;
        }
    }

    private class ListItr extends Itr<T> implements ListIterator<T> {
        private int index;
        private ListIterator<Pair<Range, T>> partitions;
        private Pair<Range, T> curPartition;


        public ListItr(int index) {
            this.index = index;
            reset();
        }

        private void reset() {
            updateModCount();
            partitions = partitions().listIterator(getPartitionIndexOf(index));
            if (partitions.hasNext()) curPartition = partitions.next();
            updateCur();
        }

        private void updateCur() {
            while (curPartition == null || index < curPartition.a.a) {
                if (!partitions.hasPrevious()) break;
                curPartition = partitions.previous();
            }
            while (curPartition == null || index >= curPartition.a.b) {
                if (!partitions.hasNext()) break;
                curPartition = partitions.next();
            }
        }

        @Override
        public boolean hasNext() {
            checkModCount();
            updateCur();
            return index < size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T res = curPartition.b;
            index++;
            updateCur();
            return res;
        }

        @Override
        public boolean hasPrevious() {
            checkModCount();
            return index > 0;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            --index;
            updateCur();
            return curPartition.b;
        }

        @Override
        public int nextIndex() {
            checkModCount();
            return index;
        }

        @Override
        public int previousIndex() {
            checkModCount();
            return index - 1;
        }

        @Override
        public void remove() {
            checkModCount();
            PartitionList.this.remove(index);
            index--;
            reset();
        }

        @Override
        public void set(T t) {
            checkModCount();
            PartitionList.this.set(index, t);
            reset();
        }

        @Override
        public void add(T t) {
            checkModCount();
            PartitionList.this.add(index, t);
            index++;
            reset();
        }
    }

    public interface PartitionIterator<T> extends Iterator<Pair<Range, T>> {
        void set(T element);
    }

    private class PartitionItr extends Itr<Pair<Range, T>> implements PartitionIterator<T> {
        private final Range fullRange;
        private final boolean reverse;
        private Range prevRange;
        private Map.Entry<Integer, T> prevEntry;
        private Map.Entry<Integer, T> nextEntry;
        private Iterator<Map.Entry<Integer, T>> mapIterator;


        public PartitionItr(Range range, boolean reverse) {
            rangeCheck(range);
            this.fullRange = new Range(range);
            this.reverse = reverse;
            reitr();
        }

        private void reitr() {
            if (prevRange != null) fullRange.a = prevRange.b;

            if (fullRange.size() <= 0) {
                nextEntry = null;
                mapIterator = Collections.emptyIterator();
                return;
            }

            Integer floorKey = map.floorKey(fullRange.a);
            Integer ceilingKey = map.ceilingKey(fullRange.b);
            if (ceilingKey == null) ceilingKey = size();

            NavigableMap<Integer, T> nmap = map.subMap(floorKey, true, ceilingKey, false);
            if (reverse) {
                mapIterator = nmap.descendingMap().entrySet().iterator();
            } else {
                mapIterator = nmap.entrySet().iterator();
            }

            if (mapIterator.hasNext()) nextEntry = mapIterator.next();
        }

        @Override
        public boolean hasNext() {
            return nextEntry != null;
        }

        @Override
        public Pair<Range, T> next() {
            checkModCount();
            if (!hasNext()) throw new NoSuchElementException();

            T lastVal;
            do {
                int lastPos = nextEntry.getKey();
                lastVal = nextEntry.getValue();
                prevEntry = nextEntry;
                if (mapIterator.hasNext()) {
                    nextEntry = mapIterator.next();
                    prevRange = new Range(lastPos, nextEntry.getKey());
                } else {
                    nextEntry = null;
                    prevRange = new Range(lastPos, PartitionList.this.size());
                }
                prevRange.intersectWith(fullRange);
            } while (prevRange.size() <= 0);

            return new Pair(prevRange, lastVal);
        }

        @Override
        public void remove() {
            checkModCount();
            if (prevRange == null) throw new NoSuchElementException();
            removeRange(prevRange);
            reitr();
            updateModCount();
        }

        @Override
        public void set(T element) {
            checkModCount();
            if (prevEntry == null) throw new NoSuchElementException();
            setRange(prevRange, element);
            reitr();
            updateModCount();
        }
    }






    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index not in bounds: " + index + " (size: " + size + ")");
        }
    }

    protected void upperRangeCheck(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index not in bounds: " + index + " (size: " + size + ")");
        }
    }

    protected void rangeCheck(Range range) {
        rangeCheck(range.a);
        upperRangeCheck(range.b);
    }
}
