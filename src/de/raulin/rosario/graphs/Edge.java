package de.raulin.rosario.graphs;

public class Edge {
	private final int from;
	private final int to;
	private final double weight;
	
	public Edge(int from, int to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public final int getFrom() {
		return from;
	}

	public final int getTo() {
		return to;
	}

	public final double getWeight() {
		return weight;
	}
	
	@Override
	public int hashCode() {
		return 23 * (from + to);
	}
	
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (other instanceof Edge) {
			Edge otherEdge = (Edge) other;
			return otherEdge.from == from && otherEdge.to == to;
		}
		return false;
	}
}
