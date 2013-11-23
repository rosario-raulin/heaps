package de.raulin.rosario.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdjacencyList implements IGraph {

	private final Set<Edge>[] edges;
	private int numberOfEdges;
	
	@SuppressWarnings("unchecked")
	public AdjacencyList(int size) {
		edges = (Set<Edge>[]) new HashSet[size];
		numberOfEdges = 0;
	}
			
	@Override
	public void insertEdge(int from, int to, double weight) {
		Edge toInsert = new Edge(from, to, weight);
		if (edges[from] == null) {
			edges[from] = new HashSet<Edge>();
		} else if (edges[from].contains(toInsert)) {
			--numberOfEdges;
			edges[from].remove(toInsert);
		}
		++numberOfEdges;
		edges[from].add(toInsert);
	}

	@Override
	public boolean hasEdge(int from, int to) {
		return edges[from].contains(new Edge(from, to, 0));
	}

	@Override
	public Iterable<Edge> getAdjacent(int from) {
		return edges[from] == null ? new ArrayList<Edge>() : edges[from];
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
