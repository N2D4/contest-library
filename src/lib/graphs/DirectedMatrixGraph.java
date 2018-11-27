package lib.graphs;

import lib.vectorization.ResizableMatrix;

public abstract class DirectedMatrixGraph extends MatrixGraph implements DirectedGraph {
    public DirectedMatrixGraph(ResizableMatrix matrix) {
        super(true, matrix);
    }

    @Override
    public int getInDegree(int vertex) {
        return super.getInDegree(vertex);
    }

    @Override
    public int getOutDegree(int vertex) {
        return super.getOutDegree(vertex);
    }
}
