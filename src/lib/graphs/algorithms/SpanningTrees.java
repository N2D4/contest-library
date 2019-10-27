package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.graphs.Graph;
import lib.graphs.UndirectedAdjacencyListGraph;
import lib.graphs.UndirectedGraph;
import lib.trees.Tree;
import lib.utils.Utils;
import lib.utils.tuples.Pair;
import lib.utils.various.UnionFind;
import sun.jvm.hotspot.utilities.Assert;

import java.util.*;

public class SpanningTrees extends Algorithm {
    private SpanningTrees() {
        // Quite dusty here...
    }

    /**
     * Uses the Kruskal algorithm for finding an MST for each component, returning the result in a graph. For the
     * (equivalent) Prim algorithm, use GraphSearch.Type.PRIM (probably in conjunction with GraphSearch.getTrees(...)).
     */
    public static UndirectedAdjacencyListGraph minimumSpanningTree(UndirectedGraph graph) {
        int n = graph.getVertexCount();
        UndirectedAdjacencyListGraph result = new UndirectedAdjacencyListGraph(n);
        UnionFind uf = new UnionFind(n);

        Graph.Edge[] edges = graph.getEdges().toArray(new Graph.Edge[0]);
        Arrays.sort(edges, Comparator.comparingDouble(e -> e.weight));
        int i = 0;
        for (Graph.Edge edge : edges) {
            if (uf.find(edge.from) != uf.find(edge.to)) {
                result.setEdgeWeight(edge.from, edge.to, edge.weight);
                uf.union(edge.from, edge.to);
                if (i >= n - 1) return result;
            }
        }

        return result;
    }

    /**
     * Uses the Kruskal algorithm for finding the cumulative weight of the MST. For the (equivalent) Prim algorithm, use
     * GraphSearch.Type.PRIM (probably in conjunction with GraphSearch.getTrees(...)).
     */
    public static double mstWeight(UndirectedGraph graph) {
        // We copy the entire code for O(m) space
        int n = graph.getVertexCount();
        double result = 0;
        UnionFind uf = new UnionFind(n);

        Graph.Edge[] edges = graph.getEdges().toArray(new Graph.Edge[0]);
        Arrays.sort(edges, Comparator.comparingDouble(e -> e.weight));
        int i = 0;
        for (Graph.Edge edge : edges) {
            if (uf.find(edge.from) != uf.find(edge.to)) {
                result += edge.weight;
                uf.union(edge.from, edge.to);
                if (i >= n - 1) return result;
            }
        }

        return result;
    }

    /**
     * Contains the Boruvska MST algorithm and variations. Usually not preferable to Kruskal due to its higher hidden
     * constant, mainly here for reference purposes
     */
    public static final class Boruvska {
        private Boruvska() {
            throw new AssertionError();
        }

        // TODO unfinished
        /*public static double mstWeight(UndirectedGraph graph) {
            int n = graph.getVertexCount();
            UnionFind uf = new UnionFind(n);
            for (int i = 0; i < n; i++) {
                int a = i;
                int minNeighbour = Utils.stream(graph.getNeighbours(a)).boxed().min(Comparator.comparingDouble(b -> graph.getEdgeWeight(a, b))).get();
                uf.union(a, minNeighbour);
            }

        }*/
    }
}
