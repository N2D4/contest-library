package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.graphs.UndirectedGraph;
import lib.trees.TreeNode;
import lib.trees.algorithms.TreeTraversal;
import lib.vectorization.VectorElementIterator;

import java.util.*;

public class GraphComponents extends Algorithm {
    private GraphComponents() {
        // Quite dusty here...
    }

    @O("n + m")
    public static boolean isConnected(UndirectedGraph graph) {
        return getComponents(graph).size() <= 1;
    }

    @O("n + m")
    public static List<List<Integer>> getComponents(UndirectedGraph graph) {
        return GraphSearch.getComponents(graph);
    }


    @O("n + m")
    public static Set<Integer> findCutVertices(UndirectedGraph graph) {
        int[] dfs = new int[graph.getVertexCount()];
        int[] low = new int[graph.getVertexCount()];

        Set<Integer> result = new HashSet<>();
        int totI = 0;
        for (GraphSearch.GraphSearchResult res : GraphSearch.getResults(graph, GraphSearch.Type.DEPTH_FIRST)) {
            List<Integer> order = TreeTraversal.preOrder(res.traversalTree);
            for (int i = 0; i < order.size(); i++) {
                dfs[order.get(i)] = totI + i;
            }




            for (int i = order.size() - 1; i >= 1; i--) {
                int v = order.get(i);
                TreeNode<Integer> node = res.nodes[v];
                low[v] = totI + i;
                for (TreeNode<Integer> child : node.getChildren()) {
                    int n = child.getValue();
                    low[v] = Math.min(low[n], low[v]);
                    if (low[n] >= dfs[v]) result.add(v);
                }

                VectorElementIterator neighbours = graph.getNeighbours(v);
                while (neighbours.hasNext()) {
                    int n = neighbours.nextInt();
                    low[v] = Math.min(dfs[n], low[v]);
                }
            }


            TreeNode<Integer> root = res.traversalTree.getRoot();
            if (root.getChildren().size() >= 2) {
                result.add(root.getValue());
            }

            totI += order.size();
        }


        /*System.out.println("tree: " + GraphSearch.getTrees(graph, GraphSearch.Type.DEPTH_FIRST));
        System.out.println("dfs: " + Arrays.toString(dfs));
        System.out.println(Arrays.toString(low));*/

        return result;
    }
}
