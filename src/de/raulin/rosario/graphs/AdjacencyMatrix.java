package de.raulin.rosario.graphs;

import java.util.LinkedList;
import java.util.List;

public class AdjacencyMatrix implements IGraph {

	private final Edge[][] edges;
	private int numberOfEdges;
	
	public AdjacencyMatrix(int size) {
		this.edges = new Edge[size][size];
		this.numberOfEdges = 0;
	}
	
	@Override
	public void insertEdge(int from, int to, double weight) {
		if (edges[from][to] == null) ++numberOfEdges;
		edges[from][to] = new Edge(from, to, weight);
	}

	@Override
	public boolean hasEdge(int from, int to) {
		return edges[from][to] != null;
	}

	@Override
	public Iterable<Edge> getAdjacent(int from) {
		List<Edge> adjacent = new LinkedList<Edge>();
		
		for (int i = 0; i < edges[from].length; ++i) {
			if (edges[from][i] != null) {
				adjacent.add(edges[from][i]);
			}
		}
		
		return adjacent;
	}

	@Override
	public int V() {
		return edges.length;
	}

	@Override
	public int E() {
		return numberOfEdges;
	}

}
