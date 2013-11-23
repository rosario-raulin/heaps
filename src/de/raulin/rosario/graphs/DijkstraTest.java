package de.raulin.rosario.graphs;

import java.util.Random;

public class DijkstraTest {
	
	private static Random RANDGEN = new Random();

	private static void makeDenseGraph(IGraph graph) {
		for (int i = 0; i < graph.V(); ++i) {
			for (int j = 0; j < graph.V(); j = j + RANDGEN.nextInt(5)+1) {
				if (i != j) {
					graph.insertEdge(i, j, RANDGEN.nextInt(1000));
				}
			}
		}
	}
	
	public static void main(String[] args) {
		int testSize = 10000;
		
		IGraph graph = new AdjacencyMatrix(testSize);
		makeDenseGraph(graph);
		
		long start = System.nanoTime();
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, 0);
		double diff = (System.nanoTime() - start) / 1000000000.0;
		
		System.out.printf("Dijkstra took %f seconds.\n", diff);
	}
}
