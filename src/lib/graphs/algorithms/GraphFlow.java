package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.graphs.DirectedGraph;
import lib.graphs.Graph;
import lib.trees.TreeNode;

public final class GraphFlow extends Algorithm {

    private GraphFlow() {
        // Quite dusty here...
    }


    /**
     * Beware of rounding errors. Might return eg. 5.9999999, which, when cast to an int, becomes 5
     *
     * Not exclusive to this algorithm but it's more common here
     */
    @O("E * min(V^2, max|f|)")
    public static double applyMaxFlow(final DirectedGraph graph, final int source, final int sink) {
        for (Graph.Edge edge : graph.getEdges()) {
            if (!graph.isEdge(edge.to, edge.from)) {
                graph.setEdgeWeight(edge.to, edge.from, 0.0);
            }
            if (edge.weight < 0) {
                graph.setEdgeWeight(edge.to, edge.from, graph.getEdgeWeight(edge.to, edge.from) - edge.weight);
                graph.setEdgeWeight(edge.from, edge.to, 0.0);
            }
        }


        TreeNode<Integer>[] treeNodeArr = new TreeNode[graph.getVertexCount()];

        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return Type.BREADTH_FIRST;
            }

            @Override
            protected TreeNode<Integer>[] getNodeArray(Graph graph) {
                return treeNodeArr;
            }

            @Override
            protected void onInspectVertex(int vertex, TreeNode<Integer> node) {
                if (vertex == sink) {
                    end(node);
                }
            }
        };

        double result = 0;
        while (!Double.isInfinite(result)) {
            TreeNode<Integer> node = (TreeNode<Integer>) search.run(graph, source);
            if (node == null) break;
            double curFlow = Double.POSITIVE_INFINITY;

            TreeNode<Integer> curNode = node;
            while (curNode.hasParent()) {
                TreeNode<Integer> curParent = curNode.getParent();
                int a1 = curParent.getValue(), a2 = curNode.getValue();
                curFlow = Math.min(curFlow, graph.getEdgeWeight(a1, a2));
                curNode = curParent;
            }


            curNode = node;
            while (curNode.hasParent()) {
                TreeNode<Integer> curParent = curNode.getParent();
                int a1 = curParent.getValue(), a2 = curNode.getValue();
                final double curFlowFinal = curFlow;
                graph.mapEdgeWeight(a1, a2, a -> a - curFlowFinal);
                graph.mapEdgeWeight(a2, a1, a -> a + curFlowFinal);
                curNode = curParent;
            }

            result += curFlow;
        }

        return result;
    }

}
