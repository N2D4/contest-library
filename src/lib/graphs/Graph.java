package lib.graphs;

import lib.utils.various.Structure;
import lib.vectorization.VectorElementIterator;

import java.util.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public interface Graph {
    int getVertexCount();
    /* BEGIN-JAVA-8 */
    default IntStream vertices() {
        return IntStream.range(0, getVertexCount());
    }
    /* END-JAVA-8 */

    int getEdgeCount();
    Iterator<Graph.Edge> edgeIterator();
    /**
     * Returns all edges in a set. If the graph is undirected, only one of (a, b) and (b, a) will be returned (undefined
     * which)
     */
    default Set<Graph.Edge> getEdges() {
        Set<Graph.Edge> result = new HashSet<Graph.Edge>();
        Iterator<Graph.Edge> iterator = edgeIterator();
        while (iterator.hasNext()) result.add(iterator.next());
        return Collections.unmodifiableSet(result);
    }

    /**
     * Returns the edge weight, or NaN if there is no edge between these two nodes
     */
    double getEdgeWeight(int v1, int v2);
    default boolean isEdge(int v1, int v2) {
         return !Double.isNaN(getEdgeWeight(v1, v2));
    }

    void setEdgeWeight(int v1, int v2, double weight);
    default void setEdge(int v1, int v2, boolean edge) {
        if (edge) {
            addEdge(v1, v2);
        } else {
            removeEdge(v1, v2);
        }
    }
    default void mapEdgeWeight(int v1, int v2, DoubleUnaryOperator mapper) {
        setEdgeWeight(v1, v2, mapper.applyAsDouble(getEdgeWeight(v1, v2)));
    }
    default void mergeEdgeWeight(int v1, int v2, double weight, DoubleBinaryOperator mapper) {
        mapEdgeWeight(v1, v2, w -> Double.isNaN(w) ? weight : mapper.applyAsDouble(w, weight));
    }
    default void addEdge(int v1, int v2) {
        if (!isEdge(v1, v2)) setEdgeWeight(v1, v2, 1.0);
    }
    default void removeEdge(int v1, int v2) {
        setEdgeWeight(v1, v2, Double.NaN);
    }
    default void toggleEdge(int v1, int v2) {
        setEdge(v1, v2, !isEdge(v1, v2));
    }

    VectorElementIterator getNeighbours(int vertex);
    default void forEachNeighbour(int vertex, EdgeConsumer action) {
        VectorElementIterator it = getNeighbours(vertex);
        while (it.hasNext()) {
            action.accept(it.next(), it.getValue());
        }
    }


    @FunctionalInterface
    interface EdgeConsumer {
        default void accept(int neighbour) {
            accept(neighbour, 1.0);
        }
        void accept(int neighbour, double weight);
    }


    class Edge extends Structure implements Comparable<Edge> {
        public final int from;
        public final int to;
        public final double weight;

        public Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            return Double.compare(this.weight, o.weight);
        }
    }
}
