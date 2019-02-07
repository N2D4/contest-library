package lib.graphs;

import lib.vectorization.ResizableMatrix;

public abstract class UndirectedMatrixGraph extends MatrixGraph implements UndirectedGraph {
    public UndirectedMatrixGraph(ResizableMatrix matrix) {
        // TODO Make sure the matrix is symmetric
        super(false, matrix);
    }

    @Override
    public int getDegree(int vertex) {
        return getInDegree(vertex);
    }
}
