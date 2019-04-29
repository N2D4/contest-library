package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.graphs.UndirectedGraph;
import lib.trees.Tree;
import lib.utils.tuples.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpanningTrees extends Algorithm {
    private SpanningTrees() {
        // Quite dusty here...
    }

    @O("n + m")
    public static Tree<Integer> findMSTPrim(UndirectedGraph graph) {
        if (!GraphComponents.isConnected(graph)) return null;
        return GraphSearch.getTree(graph, 0, GraphSearch.Type.PRIM);
    }


}
