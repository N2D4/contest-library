package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.graphs.UndirectedGraph;
import lib.utils.tuples.Pair;

import java.util.*;

public class GraphColors extends Algorithm {
    private GraphColors() {
        // Quite dusty here...
    }

    @O("n + m")
    public static boolean isBiPartite(UndirectedGraph graph) {
        return getBiPartitions(graph) != null;
    }

    @O("n + m")
    public static Pair<Set<Integer>, Set<Integer>> getBiPartitions(UndirectedGraph graph) {
        final int[] partition = new int[graph.getVertexCount()];
        partition[0] = 1;

        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return Type.BREADTH_FIRST;
            }

            @Override
            protected boolean onInspectEdge(int fromVertex, int toVertex, double weight) {
                int pf = partition[fromVertex];
                int pt = partition[toVertex];
                if (pt == 0) {
                    partition[toVertex] = pf == 1 ? 2 : 1;
                } else if (pt == pf) {
                    end(true);
                }
                return false;
            }
        };
        for (List<Integer> l : GraphSearch.getComponents(graph)) {
            if (search.run(graph, l.get(0)) != null) {
                return null;
            }
        }

        Set<Integer> a = new HashSet<Integer>(), b = new HashSet<Integer>();
        for (int i = 0; i < partition.length; i++) {
            (partition[i] == 2 ? b : a).add(i);
        }
        return new Pair<Set<Integer>, Set<Integer>>(Collections.unmodifiableSet(a), Collections.unmodifiableSet(b));
    }


}
