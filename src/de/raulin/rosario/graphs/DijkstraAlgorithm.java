package de.raulin.rosario.graphs;

import java.util.Comparator;
import java.util.LinkedList;

import de.raulin.rosario.heaps.BinaryHeap;
import de.raulin.rosario.heaps.PQNode;
import de.raulin.rosario.heaps.PriorityQueue;

public class DijkstraAlgorithm {

	private final IGraph graph;
	private final Edge[] edgeTo;
	private final PQNode<Integer>[] nodes;
	
	
	@SuppressWarnings("unchecked")
	public DijkstraAlgorithm(IGraph graph, int start) {
		this.graph = graph;
		this.edgeTo = new Edge[graph.V()];
		this.nodes = (PQNode<Integer>[]) new PQNode[graph.V()];
		findShortestPaths(start);
	}
	
	private void findShortestPaths(int start) {
		final double[] distTo = new double[graph.V()];
		PriorityQueue<Integer> pq = new BinaryHeap<Integer>(graph.V(), new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return Double.compare(distTo[o1], distTo[o2]);
			}
		});
		
		for (int v = 0; v < graph.V(); ++v) {
			distTo[v] = Integer.MAX_VALUE;
		}
		distTo[start] = 0;
		nodes[start] = pq.insert(start);
		
		while (!pq.isEmpty()) {
			relax(distTo, pq, pq.extractMin());
		}
	}
	
	private void relax(double[] distTo, PriorityQueue<Integer> pq, Integer v) {
		for (Edge e : graph.getAdjacent(v)) {
			int w = e.getTo();
			if (distTo[w] > distTo[v] + e.getWeight()) {
				distTo[w] = distTo[v] + e.getWeight();
				edgeTo[w] = e;
				if (nodes[w] != null) pq.decreaseKey(nodes[w]);
				else nodes[w] = pq.insert(w);
			}
		}
	}

	public Iterable<Integer> pathTo(int to) {
		LinkedList<Integer> path = new LinkedList<Integer>();
		
		path.add(to);
		Edge curr = edgeTo[to];
		while (curr != null) {
			path.addFirst(curr.getFrom());
			curr = edgeTo[curr.getFrom()];
		}
		
		return path;
	}
	
	public static void main(String[] args) {
		IGraph graph = new AdjacencyMatrix(5);
		graph.insertEdge(0, 1, 3);
		graph.insertEdge(0, 4, 6);
		graph.insertEdge(1, 2, 10);
		graph.insertEdge(1, 4, 1);
		graph.insertEdge(4, 2, 2);
		graph.insertEdge(2, 3, 2);
		
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, 0);
		for (int x : dijkstra.pathTo(3)) {
			System.out.println(x + " ");
		}
	}
}
