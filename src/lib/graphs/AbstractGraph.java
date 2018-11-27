package lib.graphs;

import lib.utils.Utils;
import lib.utils.tuples.Triple;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractGraph implements Graph, Serializable {

    /*
    private Map<Class<? extends Graph>, Graph> graphCache;
    private boolean isSyncCall = false;

    public AbstractGraph(int vertices) {
        invalidateCaches();
    }

    @Override
    public AdjacencyMatrixGraph asAdjacencyMatrixGraph() {
        return as(AdjacencyMatrixGraph.class, AdjacencyMatrixGraph::new);
    }

    @Override
    public AdjacencyListGraph asAdjacencyListGraph() {
        return as(AdjacencyListGraph.class, AdjacencyListGraph::new);
    }

    private <T extends Graph> T as(Class<? extends T> cl, Supplier<T> constructor) {
        if (!graphCache.containsKey(cl)) {
            T graph = constructor.get();
            graphCache.put(cl, graph);
            if (graph instanceof AbstractGraph) {
                AbstractGraph ag = (AbstractGraph) graph;
                ag.graphCache = this.graphCache;
            }
        }
        return (T) graphCache.get(cl);
    }

    private <T extends Graph> T as(Class<? extends T> cl, Function<Graph, T> constructor) {
        return as(cl, () -> constructor.apply(this));
    }

    protected void invalidateCaches() {
        if (graphCache != null) graphCache.remove(this.getClass());
        graphCache = new HashMap<>();
        graphCache.put(this.getClass(), this);
    }

    protected void forEachCached(Consumer<Graph> consumer) {
        forEachCached(consumer, this.getClass());
    }

    private void forEachCached(Consumer<Graph> consumer, Class<? extends Graph> except) {
        if (isSyncCall) {
            return;
        }
        for (Map.Entry<Class<? extends Graph>, Graph> e : graphCache.entrySet()) {
            if (e.getKey().isAssignableFrom(except)) continue;
            Graph graph = e.getValue();
            if (graph instanceof AbstractGraph) ((AbstractGraph) graph).isSyncCall = true;
            consumer.accept(graph);
            if (graph instanceof AbstractGraph) ((AbstractGraph) graph).isSyncCall = false;
        }
    }
    */


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Graph)) return false;
        Graph graph = (Graph) other;
        if (this.getVertexCount() != graph.getVertexCount()) return false;
        if (this.getEdgeCount() != graph.getEdgeCount()) return false;

        for (Iterator<Graph.Edge> iterator = this.edgeIterator(); iterator.hasNext();) {
            Graph.Edge next = iterator.next();
            int v1 = next.from, v2 = next.to;
            if (this.getEdgeWeight(v1, v2) != graph.getEdgeWeight(v1, v2)) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getVertexCount(),
                this.getEdgeCount(),
                Utils.hashAll(this.edgeIterator())
        );
    }

    protected void rangeChecks(int v1, int v2) throws IllegalArgumentException {
        if (v1 < 0 || v2 < 0 || v1 >= getVertexCount() || v2 >= getVertexCount()) throw new IllegalArgumentException();
    }



    /* BEGIN-POLYFILL-6 *../
    @Override
    public NavigableSet<Integer> getNeighbours(int vertex) {
        return getNeighbourWeights(vertex).navigableKeySet();
    }



    @Override
    public Set<Graph.Edge> getEdges() {
        Set<Graph.Edge> result = new HashSet<Graph.Edge>();
        Iterator<Graph.Edge> iterator = edgeIterator();
        while (iterator.hasNext()) result.add(iterator.next());
        return result;
    }
    /..* END-POLYFILL-6 */
}
