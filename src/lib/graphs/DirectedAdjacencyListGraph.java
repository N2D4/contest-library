package lib.graphs;

import lib.vectorization.Matrix;
import lib.vectorization.RowListSparseMatrix;

import java.util.*;

public class DirectedAdjacencyListGraph extends DirectedMatrixGraph {

    public DirectedAdjacencyListGraph(int vertices) {
        super(new RowListSparseMatrix(vertices, vertices, Double.NaN));
    }

    public DirectedAdjacencyListGraph(Matrix matrix) {
        super(new RowListSparseMatrix(matrix, Double.NaN));
    }

    public DirectedAdjacencyListGraph(Graph graph) {
        this(graph.getVertexCount());

        for (Iterator<Graph.Edge> iterator = graph.edgeIterator(); iterator.hasNext();) {
            Graph.Edge next = iterator.next();
            this.setEdgeWeight(next.from, next.to, next.weight);
        }
    }
}
