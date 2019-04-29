package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.graphs.DirectedGraph;
import lib.graphs.Graph;
import lib.trees.TreeNode;

public final class GraphFlow extends Algorithm {

    private GraphFlow() {
        // Quite dusty here...
    }


    public static double applyMaxFlow(DirectedGraph graph, int source, int sink) {
        return applyMaxFlow(graph, source, sink, 1e-8);
    }


    public static double applyMaxFlow(final DirectedGraph graph, final int source, final int sink, final double stepSize) {
        for (Graph.Edge edge : graph.getEdges()) {
            if (!graph.isEdge(edge.to, edge.from)) {
                graph.setEdgeWeight(edge.to, edge.from, 0.0);
            }
            if (edge.weight < 0) {
                graph.setEdgeWeight(edge.to, edge.from, graph.getEdgeWeight(edge.to, edge.from) - edge.weight);
                graph.setEdgeWeight(edge.from, edge.to, 0.0);
            }
        }


        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return Type.BREADTH_FIRST;
            }

            @Override
            protected boolean onInspectEdge(int from, int to, double edgeWeight) {
                return edgeWeight < stepSize;
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

            long c = Math.round(curFlow / stepSize);
            curFlow = c * stepSize;


            curNode = node;
            while (curNode.hasParent()) {
                TreeNode<Integer> curParent = curNode.getParent();
                int a1 = curParent.getValue(), a2 = curNode.getValue();
                graph.setEdgeWeight(a1, a2, graph.getEdgeWeight(a1, a2) - curFlow);
                graph.setEdgeWeight(a2, a1, graph.getEdgeWeight(a2, a1) + curFlow);
                curNode = curParent;
            }

            result += curFlow;
        }

        return result;
    }

}
