package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.generated.IntTreeNode;
import lib.graphs.DirectedGraph;
import lib.graphs.Graph;
import lib.trees.TreeNode;
import lib.utils.MathUtils;

public final class GraphFlow extends Algorithm {

    private GraphFlow() {
        // Quite dusty here...
    }


    /**
     * Applies the maximum flow from source to sink where through every edge, at most its weight can flow through.
     * Modifies the graph by writing the new capacities (note that this may create new edges, but w[i][j] + w[j][i]
     * will remain constant before and after the algorithm) to the given graph and returning a double, denoting the
     * max flow (=min cut).
     *
     * Beware of rounding errors. Might return eg. 5.9999999, which, when cast to an int, becomes 5. (Not exclusive to
     * this algorithm but it's more common here)
     */
    @O("V * E^2")
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


        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return Type.BREADTH_FIRST;
            }

            @Override
            protected IntTreeNode[] getNodeArray(Graph graph) {
                return new IntTreeNode[graph.getVertexCount()];
            }

            @Override
            protected void onInspectVertex(int vertex, IntTreeNode node) {
                if (vertex == sink) {
                    end(node);
                }
            }

            @Override
            protected boolean onInspectEdge(int v1, int v2, double weight) {
                if (weight == 0.0) {
                    return true;
                }
                return false;
            }
        };

        double result = 0;
        while (!Double.isInfinite(result)) {
            IntTreeNode node = (IntTreeNode) search.run(graph, source);
            if (node == null) break;
            double curFlow = Double.POSITIVE_INFINITY;

            IntTreeNode curNode = node;
            while (curNode.hasParent()) {
                IntTreeNode curParent = curNode.getParent();
                int a1 = curParent.getValue(), a2 = curNode.getValue();
                curFlow = Math.min(curFlow, graph.getEdgeWeight(a1, a2));
                curNode = curParent;
            }


            curNode = node;
            while (curNode.hasParent()) {
                IntTreeNode curParent = curNode.getParent();
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
