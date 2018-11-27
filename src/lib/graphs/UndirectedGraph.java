package lib.graphs;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public interface UndirectedGraph extends Graph {
    int getDegree(int vertex);
}
