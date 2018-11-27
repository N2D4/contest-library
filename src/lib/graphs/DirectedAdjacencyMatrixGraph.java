package lib.graphs;

import lib.vectorization.ArrayListMatrix;
import lib.vectorization.Matrix;

import java.util.*;

public class DirectedAdjacencyMatrixGraph extends DirectedMatrixGraph {

    public DirectedAdjacencyMatrixGraph(int vertices) {
        super(new ArrayListMatrix(vertices, vertices));
    }

    public DirectedAdjacencyMatrixGraph(Matrix matrix) {
        super(new ArrayListMatrix(matrix));
    }

    public DirectedAdjacencyMatrixGraph(Graph graph) {
        this(graph.getVertexCount());

        for (Iterator<Graph.Edge> iterator = graph.edgeIterator(); iterator.hasNext();) {
            Graph.Edge next = iterator.next();
            this.setEdgeWeight(next.from, next.to, next.weight);
        }
    }
}
