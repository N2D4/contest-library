package lib.graphs;

import lib.utils.various.Structure;
import lib.vectorization.VectorElementIterator;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Graph {
    int getVertexCount();
    /* BEGIN-JAVA-8 */
    default IntStream vertices() {
        return IntStream.range(0, getVertexCount());
    }
    /* END-JAVA-8 */

    int getEdgeCount();
    Iterator<Graph.Edge> edgeIterator();
    /* BEGIN-JAVA-8 */
    default Set<Graph.Edge> getEdges() {
        Set<Graph.Edge> result = new HashSet<Graph.Edge>();
        Iterator<Graph.Edge> iterator = edgeIterator();
        while (iterator.hasNext()) result.add(iterator.next());
        return result;
    }
    /* END-JAVA-8 */
    /* BEGIN-POLYFILL-6 *../
    Set<Graph.Edge> getEdges();
    /..* END-POLYFILL-6 */

    double getEdgeWeight(int v1, int v2);
    /* BEGIN-JAVA-8 */
    default boolean isEdge(int v1, int v2) {
         return !Double.isNaN(getEdgeWeight(v1, v2));
    }
    /* END-JAVA-8 */

    void setEdgeWeight(int v1, int v2, double weight);
    /* BEGIN-JAVA-8 */
    default void setEdge(int v1, int v2, boolean edge) {
        if (edge) {
            if (!isEdge(v1, v2)) setEdgeWeight(v1, v2, 1.0);
        } else {
            setEdgeWeight(v1, v2, Double.NaN);
        }
    }
    default void addEdge(int v1, int v2) {
        setEdge(v1, v2, true);
    }
    default void removeEdge(int v1, int v2) {
        setEdge(v1, v2, false);
    }
    /* END-JAVA-8 */

    VectorElementIterator getNeighbours(int vertex);



    class Edge extends Structure {
        public final int from;
        public final int to;
        public final double weight;

        public Edge(int from, int to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}
