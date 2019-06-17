package lib.graphs.algorithms;

import lib.algorithms.Algorithm;
import lib.algorithms.O;
import lib.graphs.DirectedGraph;
import lib.graphs.Graph;
import lib.graphs.UndirectedGraph;
import lib.trees.Tree;
import lib.trees.TreeNode;
import lib.trees.algorithms.TreeTraversal;
import lib.utils.tuples.Monad;
import lib.utils.various.Structure;
import lib.vectorization.VectorElementIterator;

import java.util.*;
import java.util.function.Supplier;

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
    private Object run(final Graph graph, final int start, final boolean directed) {
        return this.runCustomAlgorithm(() -> {
            boolean hasCycle = false;

            TreeNode<Integer>[] nodes = getNodeArray(graph);
            Map<Integer, TreeNode<Integer>> curProcessing = new HashMap<>();

            Tree<Integer> traversal = new Tree<>(start);

            Queue<TreeNode<Integer>> queue = type.newQueue(graph);
            queue.add(traversal.getRoot());

            while (!queue.isEmpty()) {
                if (shouldEnd()) return;
                TreeNode<Integer> node = queue.peek();  // To support DFS, we only remove a node the *second* time we find it
                int v = node.getValue();


                if (nodes[v] != null) {
                    queue.remove();
                    continue;
                }

                TreeNode<Integer> cp = curProcessing.get(v);

                if (cp != null) {
                    if (cp == node) {                    // Remove if it's the second time we find this node
                        nodes[v] = cp;                   // With most types of queues, "processing" is just putting all
                        queue.remove();                  // neighbours on the queue so this will instantly pop. However,
                        continue;                        // when using a stack (DFS), a node is being processed as long
                    } else {                             // as any of its child nodes are being processed. If a node is
                        hasCycle = true;                 // being processed, but a new instance of that node was found,
                        onFoundCycle(v, node, nodes[v]); // then we have found a cycle.
                        queue.remove();
                        continue;
                    }
                } else {
                    if (node instanceof TreeNode.Unattached && !node.isAttached()) {
                        ((TreeNode.Unattached) node).attach();          //  All nodes but the root are unattached
                    }
                    curProcessing.put(v, node);
                }


                onInspectVertex(v, node);

                VectorElementIterator neighbours = graph.getNeighbours(v);
                while (neighbours.hasNext()) {
                    int key = neighbours.nextInt();
                    double edgeWeight = neighbours.getValue();

                    if (onInspectEdge(v, key, edgeWeight)) continue;
                    if (!directed && node.getParent() != null && key == node.getParent().getValue()) continue;
                    if (nodes[key] != null) continue;

                    TreeNode<Integer>.Unattached child = node.createUnattached(key, edgeWeight);
                    queue.add(child);
                }
            }



            onFinish(new GraphSearchResult(traversal, nodes, hasCycle));

        });
    }


    protected abstract GraphSearch.Type getType();
    protected TreeNode<Integer>[] getNodeArray(Graph graph) {
        return new TreeNode[graph.getVertexCount()];
    }
    protected void onFoundCycle(int vertex, TreeNode<Integer> parent, TreeNode<Integer> meetsWith) {}
    protected void onInspectVertex(int vertex, TreeNode<Integer> node) {}
    protected boolean onInspectEdge(int fromVertex, int toVertex, double weight) { return false; }
    protected void onFinish(GraphSearchResult graphSearchResult) {}




    public class GraphSearchResult extends Structure {
        public final Tree<Integer> traversalTree;
        public final TreeNode<Integer>[] nodes;
        public final boolean hasCycle;

        private GraphSearchResult(Tree<Integer> traversalTree, TreeNode<Integer>[] nodes, boolean hasCycle) {
            this.traversalTree = traversalTree;
            this.nodes = nodes;
            this.hasCycle = hasCycle;
        }

    }





    public interface Type {
        Queue<TreeNode<Integer>> newQueue(Graph graph);


        GraphSearch.Type BREADTH_FIRST = graph -> new ArrayDeque<>();

        GraphSearch.Type DEPTH_FIRST = graph -> new AbstractQueue<TreeNode<Integer>>() {
            ArrayDeque<TreeNode<Integer>> deque = new ArrayDeque<>();

            @Override
            public Iterator<TreeNode<Integer>> iterator() {
                return deque.descendingIterator();
            }

            @Override
            public int size() {
                return deque.size();
            }

            @Override
            public boolean offer(TreeNode<Integer> node) {
                return deque.offerLast(node);
            }

            @Override
            public TreeNode<Integer> poll() {
                return deque.pollLast();
            }

            @Override
            public TreeNode<Integer> peek() {
                return deque.peekLast();
            }
        };

        GraphSearch.Type DIJKSTRA = graph -> new PriorityQueue<>();

        GraphSearch.Type PRIM = graph -> new PriorityQueue<>(Comparator.comparingDouble(a -> a.getDistanceToParent()));
    }



    /**
     * Finds the components of the underlying undirected graph.
     *
     */
    @O("n + m")
    public static List<List<Integer>> getComponents(UndirectedGraph graph) {      // TODO Clean-up
        final List<List<Integer>> result = new ArrayList<>();
        if (graph.getVertexCount() == 0) return result;


        final boolean[] hasVisited = new boolean[graph.getVertexCount()];

        for (GraphSearchResult res : getResults(graph, Type.BREADTH_FIRST)) {
            result.add(TreeTraversal.preOrder(res.traversalTree));
        }

        return result;
    }




    public static double getDistance(UndirectedGraph graph, int v1, int v2) {
        return getDistance(graph, v1, v2, false, false);
    }

    public static double getDistance(DirectedGraph graph, int start, int end) {
        return getDistance(graph, start, end, false, false);
    }

    public static int getEdgeDistance(UndirectedGraph graph, int v1, int v2) {
        return (int) getDistance(graph, v1, v2, false, true);
    }

    public static int getEdgeDistance(DirectedGraph graph, int start, int end) {
        return (int) getDistance(graph, start, end, false, true);
    }

    private static double getDistance(Graph graph, int start, int end, boolean directed, boolean edge) {
        GraphSearch search = new GraphSearch() {
            @Override
            protected Type getType() {
                return edge ? Type.BREADTH_FIRST : Type.DIJKSTRA;
            }

            @Override
            protected void onInspectVertex(int vertex, TreeNode<Integer> node) {
                if (vertex == end) this.end(edge ? node.getHeight() : node.getDistance());
            }
        };
        Object d = search.run(graph, start, directed);
        return d == null ? Double.POSITIVE_INFINITY : (double) d;
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
    private static GraphSearchResult getResult(Graph graph, int start, final GraphSearch.Type searchType, TreeNode<Integer>[] nodeArr, boolean directed) {
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
            protected TreeNode<Integer>[] getNodeArray(Graph graph) {
                return nodeArr == null ? super.getNodeArray(graph) : nodeArr;
            }

        };
        search.run(graph, start, directed);
        return result.value;
    }

    @O("n + m")
    private static List<GraphSearchResult> getResults(Graph graph, final GraphSearch.Type searchType, boolean directed) {
        ArrayList<GraphSearchResult> result = new ArrayList<>();

        boolean[] visited = new boolean[graph.getVertexCount()];
        TreeNode<Integer>[] nodeArr = null;
        for (int nextOne = 0; nextOne < graph.getVertexCount(); nextOne++) {
            if (visited[nextOne]) continue;

            GraphSearchResult res = getResult(graph, nextOne, searchType, nodeArr, directed);
            nodeArr = res.nodes;
            for (Integer node : TreeTraversal.preOrder(res.traversalTree)) {
                visited[node] = true;
            }
            result.add(res);
        }

        return Collections.unmodifiableList(result);
    }

    @O("n + m")
    public static List<GraphSearchResult> getResults(UndirectedGraph graph, final GraphSearch.Type searchType) {
        return getResults(graph, searchType, false);
    }

    @O("n + m")
    public static List<GraphSearchResult> getResults(DirectedGraph graph, final GraphSearch.Type searchType) {
        return getResults(graph, searchType, true);
    }

    @O("n + m")
    public static Tree<Integer> getTree(DirectedGraph graph, int start, GraphSearch.Type searchType) {
        return getResult(graph, start, searchType).traversalTree;
    }

    @O("n + m")
    public static Tree<Integer> getTree(UndirectedGraph graph, int start, GraphSearch.Type searchType) {
        return getResult(graph, start, searchType).traversalTree;
    }


    @O("n + m")
    private static List<Tree<Integer>> getTrees(Graph graph, GraphSearch.Type searchType, boolean directed) {
        List<GraphSearchResult> rs = getResults(graph, searchType, directed);
        List<Tree<Integer>> result = new ArrayList<Tree<Integer>>(rs.size());
        for (GraphSearchResult res : rs) {
            result.add(res.traversalTree);
        }
        return Collections.unmodifiableList(result);
    }

    @O("n + m")
    public static List<Tree<Integer>> getTrees(UndirectedGraph graph, final GraphSearch.Type searchType) {
        return getTrees(graph, searchType, false);
    }

    @O("n + m")
    public static List<Tree<Integer>> getTrees(DirectedGraph graph, final GraphSearch.Type searchType) {
        return getTrees(graph, searchType, true);
    }

    @O("n + m")
    public static boolean hasCycle(DirectedGraph graph) {
        return getResults(graph, Type.DEPTH_FIRST).stream().anyMatch(a -> a.hasCycle);
    }

    @O("n + m")
    public static boolean hasCycle(UndirectedGraph graph, int start, GraphSearch.Type searchType) {
        return getResults(graph, Type.DEPTH_FIRST).stream().anyMatch(a -> a.hasCycle);
    }



    // TODO update
    @O("n + m")
    public static List<Integer> getTopologicalOrder(DirectedGraph graph) {
        List<Integer> starts = new ArrayList<>();
        for (int i = 0; i < graph.getVertexCount(); i++) {
            if (graph.getInDegree(i) == 0) {
                starts.add(i);
            }
        }

        boolean[] visited = new boolean[graph.getVertexCount()];
        List<Integer> result = new ArrayList<>();
        TreeNode<Integer>[] nodeArr = null;
        for (int start : starts) {
            GraphSearchResult res = getResult(graph, start, Type.DEPTH_FIRST, nodeArr, true);
            nodeArr = res.nodes;
            if (res.hasCycle) return null;
            for (int i : TreeTraversal.postOrder(res.traversalTree)) {
                if (visited[i]) continue;
                result.add(i);
                visited[i] = true;
            }
        }
        if (result.size() < graph.getVertexCount()) return null;
        Collections.reverse(result);
        return result;
    }




}
