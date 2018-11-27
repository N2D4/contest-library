package lib.graphs;

import lib.vectorization.Matrix;
import lib.vectorization.RowListSparseMatrix;

import java.util.Iterator;

public class UndirectedAdjacencyListGraph extends UndirectedMatrixGraph {

    public UndirectedAdjacencyListGraph(int vertices) {
        super(new RowListSparseMatrix(vertices, vertices, Double.NaN));
    }

    public UndirectedAdjacencyListGraph(Matrix matrix) {
        super(new RowListSparseMatrix(matrix, Double.NaN));
    }

    public UndirectedAdjacencyListGraph(Graph graph) {
        this(graph.getVertexCount());

        for (Iterator<Edge> iterator = graph.edgeIterator(); iterator.hasNext();) {
            Edge next = iterator.next();
            this.setEdgeWeight(next.from, next.to, next.weight);
        }
    }
}
