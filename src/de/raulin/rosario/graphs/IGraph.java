package de.raulin.rosario.graphs;

public interface IGraph {
	void insertEdge(int from, int to, double weight);
	boolean hasEdge(int from, int to);
	Iterable<Edge> getAdjacent(int from);
	int V();
	int E();
}
