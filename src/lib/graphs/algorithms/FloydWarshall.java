package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.generated.IntArrayList;
import lib.graphs.Graph;
import lib.graphs.UndirectedGraph;
import lib.utils.various.Range;
import lib.vectorization.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class FloydWarshall extends Algorithm {
    private FloydWarshall() {
        // Quite dusty here...
    }


    @O("n^3")
    public static void createShortestPaths(Graph graph) {
        // TODO Line below should use weakly connected components for directed graphs instead of 1...n, don't be lazy and finally implement that
        List<? extends Iterable<Integer>> components = graph instanceof UndirectedGraph ? GraphSearch.getComponents((UndirectedGraph) graph) : Arrays.asList(new Range(0, graph.getVertexCount()));

        for (int i = 0; i < graph.getVertexCount(); i++) {
            graph.setEdgeWeight(i, i, 0.0);
        }
        
        for (Iterable<Integer> iterable : components) {
            for (int i : iterable) {
                for (int j : iterable) {
                    for (int k : iterable) {
                        double w = graph.getEdgeWeight(j, k);
                        if (graph.isEdge(j, i) && graph.isEdge(i, k)) {
                            double u = graph.getEdgeWeight(j, i) + graph.getEdgeWeight(i, k);
                            if (Double.isNaN(w)) w = u;
                            else w = Math.min(w, u);
                        }
                        graph.setEdgeWeight(j, k, w);
                    }
                }
            }
        }
    }

}
