package de.raulin.rosario.heaps;

public abstract class PQNode<T> {
	protected T element;

	public PQNode(T element) {
		this.element = element;
	}

	public T getElement() {
		return element;
	}
}
