package lib.graphs;

import lib.generated.IntArrayList;
import lib.utils.various.FunctionalIterators;
import lib.vectorization.ResizableMatrix;
import lib.vectorization.VectorElementIterator;

import java.util.*;

public abstract class MatrixGraph extends AbstractGraph implements Graph {

    private final ResizableMatrix matrix;
    private final IntArrayList inDegrees;
    private final IntArrayList outDegrees; // Null if undirected
    private int edges = 0;
    private final boolean directed;

    MatrixGraph(boolean directed, ResizableMatrix matrix) {
        if (!matrix.isSquare()) throw new IllegalArgumentException("Matrix must be square!");
        this.directed = directed;
        this.matrix = matrix;
        this.inDegrees = IntArrayList.ofSize(getVertexCount());
        if (this.directed){
            this.outDegrees = IntArrayList.ofSize(getVertexCount());
        } else {
            this.outDegrees = null;
        }
        // TODO IMPORTANT Support for non-zero matrices (right now meta information like edges isn't updated)
        // instead we just set the matrix to zeroes for now (NaN actually)
        this.matrix.setAll(Double.NaN);
    }

    public ResizableMatrix getMatrix() {
        // TODO Unmodifiable matrix?
        return matrix;
    }

    @Override
    public int getVertexCount() {
        return matrix.getRowCount();
    }

    @Override
    public int getEdgeCount() {
        return edges;
    }


    @Override
    public Iterator<Graph.Edge> edgeIterator() {
        return new Iterator<Edge>() {
            private int vertex = -1;
            private VectorElementIterator iterator = null;
            private Edge nxt = null;

            @Override
            public boolean hasNext() {
                int n = getVertexCount();
                if (nxt == null) {
                    do {
                        while (iterator == null || !iterator.hasNext()) {
                            if (vertex >= n-1) return false;
                            vertex++;
                            iterator = getNeighbours(vertex);
                        }
                        nxt = new Edge(vertex, iterator.nextInt(), iterator.getValue());
                    } while (directed || nxt.to >= nxt.from);
                }
                return true;
            }

            @Override
            public Edge next() {
                if (!hasNext()) throw new NoSuchElementException();
                Edge res = nxt;
                nxt = null;
                return res;
            }
        };
    }

    @Override
    public double getEdgeWeight(int v1, int v2) {
        rangeChecks(v1, v2);
        return matrix.get(v1, v2);
    }

    @Override
    public void setEdgeWeight(int v1, int v2, double weight) {
        rangeChecks(v1, v2);

        double d = weight;

        int weightZ = Double.isNaN(d) ? 0 : 1;
        int prevWeightZ = Double.isNaN(this.matrix.set(v1, v2, d)) ? 0 : 1;
        int change = weightZ - prevWeightZ;

        edges += change;
        if (!directed) {
            this.matrix.set(v2, v1, d);
        }

        inDegrees.set(v2, inDegrees.get(v2) + change);
        if (directed) {
            outDegrees.set(v1, outDegrees.get(v1) + change);
        } else {
            inDegrees.set(v1, inDegrees.get(v1) + change);
        }
    }

    @Override
    public VectorElementIterator getNeighbours(int vertex) {
        return this.matrix.getRowNonValued(vertex, Double.NaN);
    }


    int getInDegree(int vertex) {
        return inDegrees.get(vertex);
    }

    int getOutDegree(int vertex) {
        return outDegrees.get(vertex);
    }


    @Override
    public String toString() {
        return matrix.toString();
    }
}
