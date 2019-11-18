package lib.graphs;

import lib.utils.Utils;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractGraph implements Graph, Serializable {

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getVertexCount(); i++) {
            if (i != 0) result.append("\n");
            for (int j = 0; j < getVertexCount(); j++) {
                if (j != 0) result.append(", ");
                result.append(this.getEdgeWeight(i, j));
            }
        }

        return result.toString();
    }

    protected void rangeChecks(int v1, int v2) throws IllegalArgumentException {
        int V = getVertexCount();
        if (v1 < 0 || v1 >= V) throw new IllegalArgumentException("First argument " + v1 + " out of range [0, " + V + "]!");
        if (v2 < 0 || v2 >= V) throw new IllegalArgumentException("Second argument " + v2 + " out of range [0, " + V + "]!");
    }
}
