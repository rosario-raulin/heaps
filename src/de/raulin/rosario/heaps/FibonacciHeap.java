package de.raulin.rosario.heaps;

import java.util.Comparator;

public class FibonacciHeap<T> implements PriorityQueue<T> {

	class FNode extends PQNode<T> {
		private FNode parent;
		private boolean marked;
		private DLinkedList<FNode> children;
		private DLinkedList<FNode>.DNode node;

		public FNode(T element) {
			super(element);
			this.parent = null;
			this.marked = false;
			this.children = new DLinkedList<FNode>();
			this.node = null;
		}

		public DLinkedList<FNode> getChildren() {
			return children;
		}

		public int degree() {
			return children.size();
		}

		public FNode left() {
			return node.getPrev().getData();
		}

		public FNode right() {
			return node.getNext().getData();
		}

		public boolean isMarked() {
			return marked;
		}

		public FNode parent() {
			return parent;
		}
	}

	private FNode root;
	private int size;
	private DLinkedList<FNode> roots;
	private final Comparator<T> comp;

	/**
	 * Creates an empty Fibonacci heap.
	 */
	public FibonacciHeap(Comparator<T> comp) {
		this.size = 0;
		this.roots = null;
		this.comp = comp;
	}

	/**
	 * This method returns the smallest element or null if the heap is empty. It
	 * does not change the heap so two successive calls two min will return the
	 * same element.
	 * <p>
	 * Note: Its worst-case runtime is O(1).
	 * 
	 * @return the smallest element or null if empty
	 */
	@Override
	public T min() {
		return root != null ? root.element : null;
	}

	/**
	 * Returns the smallest element or null if the heap is empty. It
	 * consolidates the heap so that the next call to extractMin returns the
	 * second smallest element (or null if the heap is empty).
	 * <p>
	 * Note: Its amortized runtime is O(log(n)).
	 * 
	 * @return the smallest element or null if empty
	 */
	@Override
	public T extractMin() {
		FNode min = root;

		if (min != null) {
			// We first add all the children of the root
			// to the roots list.
			for (FNode child : min.children) {
				child.node = roots.push_back(child);
				child.parent = null;
			}
			// Now we remove the current root.
			roots.delete(min.node);
			if (min == min.right()) {
				// There is no other element left, so the root is null.
				// The heap is now empty.
				root = null;
			} else {
				// Before consolidating the roots list, we have to
				// make sure that root != min, so we simply set
				// root to its right sibling. This is not necessarily
				// the minimum, but consolidate() will set the root
				// appropriately.
				root = min.right();
				consolidate();
			}
			--size;
			return min.element;
		} else {
			// The heap is already empty.
			assert (size == 0);
			return null;
		}
	}

	/**
	 * Returns the FNode at position pos in the auxiliary array aux.
	 * <p>
	 * Note: The caller has to make sure that aux consists of FNodes.
	 * 
	 * @param aux
	 *            the auxiliary array of FNodes
	 * @param pos
	 *            the position to get
	 * @return FNode at aux[pos] or null
	 */
	@SuppressWarnings("unchecked")
	private FNode get(Object[] aux, int pos) {
		if (aux[pos] == null)
			return null;
		else
			return (FNode) aux[pos];
	}

	private static final double PHI = (1 + Math.sqrt(5)) / 2;

	/**
	 * We can show that the maximum degree D(n) for a Fibonacci heap with n
	 * nodes is at most floor(logPhi(n)) where logPhi(n) is the logarithm to the
	 * base of Phi (the golden ratio).
	 * 
	 * This method returns the upper bound of this heap's maximum degree (i. e.
	 * floor(logPhi(size))).
	 * 
	 * @return floor(logPhi(size))
	 */
	private int getAuxSize() {
		double logSize = Math.log(size) / Math.log(PHI);
		return (int) Math.round(Math.floor(logSize));
	}

	/**
	 * Makes sure the degree of all nodes in the roots list are different by
	 * linking nodes of equal degree.
	 */
	private void consolidate() {
		Object[] temp = new Object[getAuxSize() + 1];

		for (FNode r : roots) {
			FNode x = r;
			int degree = x.degree();

			while (temp[degree] != null) {
				// A root with the same degree exists
				FNode y = get(temp, degree);
				// Now we have to make sure x <= y.
				if (more(x.element, y.element)) {
					FNode tmp = x;
					x = y;
					y = tmp;
				}
				// Now we link y to x.
				link(y, x);
				temp[degree] = null;
				++degree;
			}
			temp[degree] = x;
		}

		// Now we have to determine the new root: It is the smallest
		// element in our auxiliary array. All the other elements in
		// there are inserted in the roots list.
		root = null;
		for (int i = 0; i < temp.length; ++i) {
			if (temp[i] != null) {
				FNode curr = get(temp, i);
				if (root == null) {
					// We found the first element != null in our
					// auxiliary array.
					roots = new DLinkedList<FNode>();
					curr.node = roots.push_back(curr);
					root = curr;
				} else {
					curr.node = roots.push_back(curr);
					if (less(curr.element, root.element)) {
						// We found a smaller element, so that's our root.
						root = curr;
					}
				}
			}
		}

		assert (root != null);
	}

	/**
	 * Links FNode y to FNode x, i. e. makes y a child of x and deletes y from
	 * the list of roots. It removes the mark of y.
	 * 
	 * @param y
	 *            the FNode that should be linked to x
	 * @param x
	 *            the FNode y should be linked to
	 */
	private void link(FNode y, FNode x) {
		roots.delete(y.node);
		y.node = x.children.push_back(y);
		y.parent = x;
		y.marked = false;
	}

	/**
	 * Inserts the element by creating a new FNode and inserting it into the
	 * list of roots.
	 * <p>
	 * Note: Its worst-case runtime is O(1).
	 * 
	 * @param element
	 *            the element to be inserted
	 * @return the FNode representing the element
	 */
	@Override
	public PQNode<T> insert(T element) {
		FNode toInsert = new FNode(element);

		if (root == null) {
			// We just have to create a new roots list
			// and insert the new node there.
			// That new node if, of course, the root.
			roots = new DLinkedList<FNode>();
			toInsert.node = roots.push_back(toInsert);
			root = toInsert;
		} else {
			toInsert.node = roots.push_back(toInsert);
			if (less(element, root.element)) {
				// We found a new minimum, so that's the root.
				root = toInsert;
			}
		}

		++size;

		return toInsert;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Restores the heap order after x's key decreased.
	 * 
	 * @param x
	 *            the FNode with decreased key
	 */
	private void decreaseKey(FNode x) {
		FNode y = x.parent;
		if (y != null && less(x.element, y.element)) {
			cut(x, y);
			ccut(y);
		}
		if (less(x.element, root.element)) {
			root = x;
		}
	}

	private void ccut(FNode y) {
		FNode parent = y.parent;
		if (parent != null) {
			if (parent.marked) {
				cut(y, parent);
				ccut(parent);
			} else {
				y.marked = true;
			}
		}
	}

	/**
	 * Removes x from y's children list and makes it a root.
	 * 
	 * @param x
	 *            the element to be removed from y's children list
	 * @param y
	 *            the current parent of x
	 */
	private void cut(FNode x, FNode y) {
		y.children.delete(x.node);
		x.node = roots.push_back(x);
		x.parent = null;
		x.marked = false;
	}

	/**
	 * Rearranges the queue after element's priority decreased.
	 * <p>
	 * Note: This method does not work if element's priority increased! Its
	 * amortized runtime is O(1).
	 */
	@Override
	public void decreaseKey(PQNode<T> element) {
		if (element.getClass() == FNode.class) {
			decreaseKey((FNode) element);
		} else {
			assert (false);
		}
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	private boolean less(T x, T y) {
		return comp.compare(x, y) < 0;
	}
	
	private boolean more(T x, T y) {
		return comp.compare(x, y) > 0;
	}
}
