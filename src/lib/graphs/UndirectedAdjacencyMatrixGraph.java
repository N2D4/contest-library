package lib.graphs;

import lib.vectorization.ArrayListMatrix;
import lib.vectorization.Matrix;

import java.util.Iterator;

public class UndirectedAdjacencyMatrixGraph extends UndirectedMatrixGraph {

    public UndirectedAdjacencyMatrixGraph(int vertices) {
        super(new ArrayListMatrix(vertices, vertices));
    }

    public UndirectedAdjacencyMatrixGraph(Matrix matrix) {
        super(new ArrayListMatrix(matrix));
    }

    public UndirectedAdjacencyMatrixGraph(Graph graph) {
        this(graph.getVertexCount());

        for (Iterator<Edge> iterator = graph.edgeIterator(); iterator.hasNext();) {
            Edge next = iterator.next();
            this.setEdgeWeight(next.from, next.to, next.weight);
        }
    }
}
