package lib.graphs;

public interface DirectedGraph extends Graph {
    int getInDegree(int vertex);
    int getOutDegree(int vertex);

}
