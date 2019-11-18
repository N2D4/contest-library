package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.generated.IntArrayList;
import lib.generated.IntList;
import lib.generated.IntTree;
import lib.generated.IntTreeNode;
import lib.graphs.DirectedGraph;
import lib.graphs.Graph;
import lib.graphs.UndirectedGraph;
import lib.trees.Tree;
import lib.trees.TreeNode;
import lib.utils.QueueUtils;
import lib.utils.Utils;
import lib.utils.tuples.Monad;
import lib.utils.tuples.Pair;
import lib.utils.various.Structure;
import lib.vectorization.VectorElementIterator;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GraphSearch extends Algorithm {

    private final GraphSearch.Type type = getType();


    @O("n + m")
    public Object run(DirectedGraph graph, int start) {
        return run(graph, start, true);
    }

    @O("n + m")
    public Object run(UndirectedGraph graph, int start) {
        return run(graph, start, false);
    }

    @O("n + m")
    Object run(final Graph graph, final int start, final boolean directed) {
        return this.runCustomAlgorithm(() -> {
            boolean doCycleDetection = this.doCycleDetection();
            if (doCycleDetection && !type.canDetectCycles()) {
                throw new IllegalArgumentException("Attempted to do cycle detection with a graph search type that does not support it! (Try DFS)");
            }

            IntTreeNode[] nodes = getNodeArray(graph);
            Map<Integer, IntTreeNode> curProcessing = doCycleDetection ? new HashMap<>() : null;

            IntTree traversal = new IntTree(start, customDistanceFolder());

            Queue<IntTreeNode> queue = type.newQueue(graph);
            queue.add(traversal.getRoot());

            while (!queue.isEmpty()) {
                if (shouldEnd()) return;

                IntTreeNode node = queue.peek();  // To support cycle detection we only remove a node the *second* time we find it
                int v = node.getValue();


                if (nodes[v] != null && nodes[v].getTree() == traversal) {
                    queue.remove();
                    continue;
                }

                if (doCycleDetection) {
                    IntTreeNode cp = curProcessing.get(v);

                    if (cp != null) {
                        if (cp == node) {                    // Remove if it's the second time we find this node
                            nodes[v] = cp;                   // With most types of queues, "processing" is just putting all
                            queue.remove();                  // neighbours on the queue so this will instantly pop. However,
                            continue;                        // when using a stack (DFS), a node is being processed as long
                        } else {                             // as any of its child nodes are being processed. If a node is
                            onFoundCycle(v, node, nodes[v]); // being processed, but a new instance of that node was found,
                            queue.remove();                  // then we have found a cycle.
                            continue;
                        }
                    } else {
                        if (node instanceof IntTreeNode.Unattached && !node.isAttached()) {
                            ((IntTreeNode.Unattached) node).attach();          //  All nodes but the root are unattached
                        }
                        curProcessing.put(v, node);
                    }
                } else {
                    nodes[v] = node;
                    if (node instanceof IntTreeNode.Unattached && !node.isAttached()) {
                        ((IntTreeNode.Unattached) node).attach();
                    }
                    queue.remove();
                }


                onInspectVertex(v, node);

                VectorElementIterator neighbours = graph.getNeighbours(v);
                while (neighbours.hasNext()) {
                    int key = neighbours.next();
                    double edgeWeight = neighbours.getValue();

                    if (onInspectEdge(v, key, edgeWeight)) continue;
                    if (!directed && node.getParent() != null && key == node.getParent().getValue()) continue;
                    if (nodes[key] != null) continue;

                    IntTreeNode.Unattached child = node.createUnattached(key, edgeWeight);
                    queue.add(child);
                }
            }



            onFinish(new GraphSearchResult(traversal, nodes));

        });
    }


    protected abstract GraphSearch.Type getType();
    /**
     * Has to be an array of the right size (number of vertices). It doesn't need to be empty (so arrays from previous
     * graph searches can be re-used). Entries of nodes that were never visited will not be modified
     */
    protected IntTreeNode[] getNodeArray(Graph graph) {
        return new IntTreeNode[graph.getVertexCount()];
    }
    protected void onFoundCycle(int vertex, IntTreeNode parent, IntTreeNode meetsWith) {}
    protected void onInspectVertex(int vertex, IntTreeNode node) {}
    protected boolean onInspectEdge(int fromVertex, int toVertex, double weight) { return false; }
    protected void onFinish(GraphSearchResult graphSearchResult) {}
    protected boolean doCycleDetection() {
        return false;
    }
    protected Pair<Double, DoubleBinaryOperator> customDistanceFolder() {
        return new Pair<>(0.0, Double::sum);
    }




    public class GraphSearchResult extends Structure {
        public final IntTree traversalTree;
        public final IntTreeNode[] nodes;

        private GraphSearchResult(IntTree traversalTree, IntTreeNode[] nodes) {
            this.traversalTree = traversalTree;
            this.nodes = nodes;
        }

    }





    public interface Type {
        Queue<IntTreeNode> newQueue(Graph graph);
        default boolean canDetectCycles() {
            return false;
        }

        static GraphSearch.Type BREADTH_FIRST = graph -> QueueUtils.createFIFO();

        static GraphSearch.Type DEPTH_FIRST = new Type() {
            @Override
            public boolean canDetectCycles() {
                return true;
            }

            @Override
            public Queue<IntTreeNode> newQueue(Graph graph) {
                return QueueUtils.createLIFO();
            }
        };

        static GraphSearch.Type DIJKSTRA = graph -> QueueUtils.createPriority(a -> a.getDistance());

        static GraphSearch.Type PRIM = graph -> QueueUtils.createPriority(a -> a.getDistanceToParent());
    }



    /**
     * Finds the components of the underlying undirected graph.
     *
     */
    @O("n + m")
    public static Set<Set<Integer>> getComponents(UndirectedGraph graph) {      // TODO Clean-up
        if (graph.getVertexCount() == 0) return Collections.emptySet();

        final List<Set<Integer>> result = new ArrayList<>();
        for (GraphSearchResult res : getResults(graph, Type.BREADTH_FIRST)) {
            result.add(res.traversalTree.preOrder().stream().map(a -> a.getValue()).collect(Utils.collectToSet()));
        }
        return Utils.asSet(result);
    }



    @O("n + m")
    private static <T> Iterator<T> runSearchForAllComponents(BiFunction<Integer, IntTreeNode[], T> search, Graph graph, boolean directed) {
        return new Iterator<T>() {
            IntTreeNode[] nodeArr = new IntTreeNode[graph.getVertexCount()];
            int nextOne = 0;

            @Override
            public boolean hasNext() {
                while (true) {
                    if (nextOne >= nodeArr.length) return false;
                    if (nodeArr[nextOne] == null) return true;
                    nextOne++;
                }
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return search.apply(nextOne, nodeArr);
            }
        };
    }

    @O("n + m")
    public static double getDistance(UndirectedGraph graph, int v1, int v2) {
        return getDistance(graph, v1, v2, false, false);
    }

    @O("n + m")
    public static double getDistance(DirectedGraph graph, int start, int end) {
        return getDistance(graph, start, end, false, false);
    }

    @O("n + m")
    public static int getEdgeDistance(UndirectedGraph graph, int v1, int v2) {
        return (int) getDistance(graph, v1, v2, false, true);
    }

    @O("n + m")
    public static int getEdgeDistance(DirectedGraph graph, int start, int end) {
        return (int) getDistance(graph, start, end, false, true);
    }

    @O("n + m")
    private static double getDistance(Graph graph, int start, int end, boolean directed, boolean edge) {
        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return edge ? Type.BREADTH_FIRST : Type.DIJKSTRA;
            }

            @Override
            protected void onInspectVertex(int vertex, IntTreeNode node) {
                if (this.shouldEnd()) return;
                if (vertex == end) this.end(edge ? node.getHeight() : node.getDistance());
            }
        };
        Object d = search.run(graph, start, directed);
        return d == null ? Double.POSITIVE_INFINITY : (double) d;
    }

    @O("n + m")
    public static double[] getDistances(UndirectedGraph graph, int v1) {
        return getDistances(graph, v1, false);
    }

    @O("n + m")
    public static double[] getDistances(DirectedGraph graph, int start) {
        return getDistances(graph, start, true);
    }

    @O("n + m")
    public static int[] getEdgeDistances(UndirectedGraph graph, int v1) {
        return getEdgeDistances(graph, v1, false);
    }

    @O("n + m")
    public static int[] getEdgeDistances(DirectedGraph graph, int start) {
        return getEdgeDistances(graph, start, true);
    }

    @O("n + m")
    private static int[] getEdgeDistances(Graph graph, int start, boolean directed) {
        return Arrays.stream(getResult(graph, start, Type.BREADTH_FIRST, directed).nodes).mapToInt(a -> a == null ? Integer.MAX_VALUE : a.getHeight()).toArray();
    }

    @O("n + m")
    private static double[] getDistances(Graph graph, int start, boolean directed) {
        return Arrays.stream(getResult(graph, start, Type.DIJKSTRA, directed).nodes).mapToDouble(a -> a == null ? Double.POSITIVE_INFINITY : a.getDistance()).toArray();
    }


    @O("n + m")
    public static GraphSearchResult getResult(DirectedGraph graph, int start, final GraphSearch.Type searchType) {
        return getResult(graph, start, searchType, true);
    }


    @O("n + m")
    public static GraphSearchResult getResult(UndirectedGraph graph, int start, final GraphSearch.Type searchType) {
        return getResult(graph, start, searchType, false);
    }

    @O("n + m")
    private static GraphSearchResult getResult(Graph graph, int start, final GraphSearch.Type searchType, boolean directed) {
        return getResult(graph, start, searchType, null, directed);
    }

    @O("n + m")
    private static GraphSearchResult getResult(Graph graph, int start, final GraphSearch.Type searchType, IntTreeNode[] nodeArr, boolean directed) {
        final Monad<GraphSearchResult> result = new Monad<>(null);
        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return searchType;
            }

            @Override
            protected void onFinish(GraphSearchResult res) {
                result.value = res;
            }

            @Override
            protected IntTreeNode[] getNodeArray(Graph graph) {
                return nodeArr == null ? super.getNodeArray(graph) : nodeArr;
            }

        };
        search.run(graph, start, directed);
        return result.value;
    }

    @O("n + m")
    private static Set<GraphSearchResult> getResults(Graph graph, final GraphSearch.Type searchType, boolean directed) {
        return Utils.asSet(Utils.toArrayList(runSearchForAllComponents((n, nodeArr) -> getResult(graph, n, searchType, nodeArr, directed), graph, directed)));
    }

    @O("n + m")
    public static Set<GraphSearchResult> getResults(UndirectedGraph graph, final GraphSearch.Type searchType) {
        return getResults(graph, searchType, false);
    }

    @O("n + m")
    public static Set<GraphSearchResult> getResults(DirectedGraph graph, final GraphSearch.Type searchType) {
        return getResults(graph, searchType, true);
    }

    @O("n + m")
    public static IntTree getTree(DirectedGraph graph, int start, GraphSearch.Type searchType) {
        return getResult(graph, start, searchType).traversalTree;
    }

    @O("n + m")
    public static IntTree getTree(UndirectedGraph graph, int start, GraphSearch.Type searchType) {
        return getResult(graph, start, searchType).traversalTree;
    }


    @O("n + m")
    private static Set<IntTree> getTrees(Graph graph, GraphSearch.Type searchType, boolean directed) {
        Set<GraphSearchResult> rs = getResults(graph, searchType, directed);
        List<IntTree> result = new ArrayList<IntTree>(rs.size());
        for (GraphSearchResult res : rs) {
            result.add(res.traversalTree);
        }
        return Utils.asSet(result);
    }

    @O("n + m")
    public static Set<IntTree> getTrees(UndirectedGraph graph, final GraphSearch.Type searchType) {
        return getTrees(graph, searchType, false);
    }

    @O("n + m")
    public static Set<IntTree> getTrees(DirectedGraph graph, final GraphSearch.Type searchType) {
        return getTrees(graph, searchType, true);
    }

    @O("n + m")
    public static boolean hasCycle(DirectedGraph graph) {
        return hasCycle(graph, true);
    }

    @O("n + m")
    public static boolean hasCycle(UndirectedGraph graph) {
        return hasCycle(graph, false);
    }

    @O("n + m")
    private static boolean hasCycle(Graph graph, boolean directed) {
        return Utils.stream(runSearchForAllComponents((start, nodeArr) -> new GraphSearch() {
            @Override
            protected Type getType() {
                return Type.DEPTH_FIRST;
            }

            @Override
            protected boolean doCycleDetection() {
                return true;
            }

            @Override
            protected void onFoundCycle(int vertex, IntTreeNode parent, IntTreeNode meetsWith) {
                this.end(true);
            }

            @Override
            protected IntTreeNode[] getNodeArray(Graph graph) {
                return nodeArr == null ? super.getNodeArray(graph) : nodeArr;
            }
        }.run(graph, start, directed), graph, directed)).anyMatch(o -> o != null);
    }



    @O("n + m")
    public static IntList getTopologicalOrder(DirectedGraph graph) {
        if (hasCycle(graph)) return null;
        IntList res = new IntArrayList();
        for (IntTree tree : getTrees(graph, Type.DEPTH_FIRST)) {
            for (IntTreeNode node : tree.postOrder()) {
                res.add(node.getValue());
            }
        }
        res.reverse();
        return res;
    }




}
