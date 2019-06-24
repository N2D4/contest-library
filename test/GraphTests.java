import lib.graphs.*;
import lib.graphs.algorithms.FloydWarshall;
import lib.graphs.algorithms.GraphComponents;
import lib.graphs.algorithms.GraphSearch;
import lib.graphs.algorithms.SpanningTrees;
import lib.trees.Tree;
import lib.trees.TreeNode;
import lib.trees.algorithms.TreeTraversal;
import lib.utils.MathUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTests {

    List<Function<Integer, DirectedGraph>> directedGraphs = Arrays.asList(
        DirectedAdjacencyListGraph::new,
        DirectedAdjacencyMatrixGraph::new
    );

    List<Function<Integer, UndirectedGraph>> undirectedGraphs = Arrays.asList(
            UndirectedAdjacencyListGraph::new,
            UndirectedAdjacencyMatrixGraph::new
    );

    List<Function<Integer, Graph>> graphs = Arrays.asList(
            DirectedAdjacencyListGraph::new,
            DirectedAdjacencyMatrixGraph::new,
            UndirectedAdjacencyListGraph::new,
            UndirectedAdjacencyMatrixGraph::new
    );






    @Test
    public void topologicalSorting() {
        Random random = new Random("top sorting test".hashCode());
        Iterator<DirectedGraph> iterator = genDirectedGraph(50, 50, random).iterator();
        outer: for (int it = 0; it < 1000 * TestConstants.SCALE; it++) {
            DirectedGraph graph = iterator.next();
            List<Integer> top = GraphSearch.getTopologicalOrder(graph);
            if (top == null) {
                assertTrue(GraphSearch.hasCycle(graph));
                continue;
            }
            for (int i : top) {
                assertTrue(!GraphSearch.hasCycle(graph));
                assertTrue(graph.getInDegree(i) == 0);

                for (int j = 0; j < graph.getVertexCount(); j++) {
                    graph.removeEdge(i, j);
                }
            }
        }
    }


    @Test
    public void primKruskalMSTConsistency() {
        Random random = new Random("prim and kruskal MST consistency".hashCode());
        Iterator<UndirectedGraph> iterator = genUndirectedGraph(150, random).iterator();
        outer: for (int it = 0; it < 100 * TestConstants.SCALE; it++) {
            UndirectedGraph graph = iterator.next();
            int n = graph.getVertexCount();
            int components = GraphComponents.getComponents(graph).size();

            /*System.err.println();
            System.err.println();
            System.err.println();
            System.err.println();
            System.err.println();
            System.err.println();
            System.err.println(graph.getClass());
            System.err.println(graph);
            System.err.println();*/

            UndirectedGraph kruskal = SpanningTrees.minimumSpanningTree(graph);
            //System.err.println(kruskal);
            //System.err.println();
            assertEquals(kruskal.getEdgeCount(), n - components);

            Set<Tree<Integer>> prim = GraphSearch.getTrees(graph, GraphSearch.Type.PRIM);
            //System.err.println(prim);
            //System.err.println();
            assertEquals(prim.size(), components);


            double primSize = prim.stream().flatMapToDouble(a -> TreeTraversal.preOrder(a).stream().filter(b -> b.hasParent()).mapToDouble(b -> b.getDistanceToParent())).sum();
            double kruskalSize = kruskal.getEdges().stream().mapToDouble(a -> a.weight).sum();
            assertEquals(primSize, kruskalSize, 0.00001);
        }
    }




    @Test
    public void floydWarshallDijkstraConsistency() {
        Random random = new Random("floyd warshall and dijkstra consistency".hashCode());
        Iterator<Graph> iterator = genGraph(35, random).iterator();
        outer: for (int it = 0; it < 50 * TestConstants.SCALE; it++) {
            Graph graph = iterator.next();
            for (int i = 0; i < graph.getVertexCount(); i++) {
                for (int j = 0; j < graph.getVertexCount(); j++) {
                    if (!graph.isEdge(i, j)) continue;
                    graph.setEdgeWeight(i, j, Math.abs(graph.getEdgeWeight(i, j)));
                }
            }

            /*System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(graph.getClass().getName());
            System.out.println(graph);
            System.out.println();*/
            
            double[][] pathLength = new double[graph.getVertexCount()][graph.getVertexCount()];
            for (int i = 0; i < graph.getVertexCount(); i++) {
                for (int j = 0; j < graph.getVertexCount(); j++) {
                    if (graph instanceof UndirectedGraph) {
                        //if (i == 0 && j == 1) System.out.println(GraphSearch.getTree((UndirectedAdjacencyListGraph) graph, i, GraphSearch.Type.DIJKSTRA));
                        pathLength[i][j] = GraphSearch.getDistance((UndirectedGraph) graph, i, j);
                    } else if (graph instanceof DirectedGraph) {
                        pathLength[i][j] = GraphSearch.getDistance((DirectedGraph) graph, i, j);
                    } else continue outer;
                }
            }


            FloydWarshall.createShortestPaths(graph);


            /*System.out.println("dijkstra:");
            for (int i = 0; i < pathLength.length; i++) {
                for (int j = 0; j < pathLength[i].length; j++) {
                    System.out.print(pathLength[i][j] + ", ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("fw:");
            System.out.println(graph);
            System.out.println();*/


            for (int i = 0; i < graph.getVertexCount(); i++) {
                for (int j = 0; j < graph.getVertexCount(); j++) {
                    double w = graph.getEdgeWeight(i, j);
                    assertEquals(pathLength[i][j], Double.isNaN(w) ? Double.POSITIVE_INFINITY : w, 0.001, i + " " + j);
                }
            }
        }
    }











    private Stream<DirectedGraph> genDirectedGraph(int nCap, Random random) {
        return genDirectedGraph(nCap, -1, random);
    }

    private Stream<DirectedGraph> genDirectedGraph(int nCap, int mCap, Random random) {
        return genGraph(nCap, mCap, random, directedGraphs);
    }

    private Stream<UndirectedGraph> genUndirectedGraph(int nCap, Random random) {
        return genUndirectedGraph(nCap, -1, random);
    }

    private Stream<UndirectedGraph> genUndirectedGraph(int nCap, int mCap, Random random) {
        return genGraph(nCap, mCap, random, undirectedGraphs);
    }

    private Stream<Graph> genGraph(int nCap, Random random) {
        return genGraph(nCap, -1, random);
    }

    private Stream<Graph> genGraph(int nCap, int mCap, Random random) {
        return genGraph(nCap, mCap, random, graphs);
    }



    private <T extends Graph> Stream<T> genGraph(int nCap, int mCap, Random random, List<Function<Integer, T>> suppliers) {
        return Stream.generate(() -> {
            int n = random.nextInt(nCap - 1) + 1;
            int m = random.nextInt(mCap > 0 ? mCap : MathUtils.sq(random.nextInt(n)) + 1);
            Function<Integer, T> supplier = suppliers.get(random.nextInt(suppliers.size()));

            T graph = supplier.apply(n);
            for (int i = 0; i < m; i++) {
                graph.setEdgeWeight(random.nextInt(n), random.nextInt(n), random.nextDouble() * 1000 - 500);
            }

            return graph;
        });
    }
}
