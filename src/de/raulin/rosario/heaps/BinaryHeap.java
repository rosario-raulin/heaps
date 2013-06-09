package de.raulin.rosario.heaps;

public class BinaryHeap<T extends Comparable<T>> implements PriorityQueue<T> {

	private final static int UNKNOWN_POS = -1;
	private static final int DEFAULT_SIZE = 16;

	class BinaryNode extends PQNode<T> {
		private int pos;

		public BinaryNode(T element) {
			super(element);
			this.pos = UNKNOWN_POS;
		}

		public int getPos() {
			return pos;
		}
	}

	private int size;
	private Object[] data;

	public BinaryHeap() {
		this(DEFAULT_SIZE);
	}

	public BinaryHeap(int size) {
		this.size = 0;
		this.data = new Object[size + 1];
	}

	private void resize(int to) {
		Object[] newData = new Object[to];

		for (int i = 0; i < data.length; ++i) {
			newData[i] = data[i];
		}

		data = newData;
	}

	@SuppressWarnings("unchecked")
	private BinaryNode get(Object[] aux, int pos) {
		return (BinaryNode) aux[pos];
	}

	@Override
	public T min() {
		if (size == 0)
			return null;
		else
			return get(data, 1).element;
	}

	@Override
	public T extractMin() {
		if (size > 0) {
			T min = get(data, 1).element;

			exch(1, size--);
			data[size + 1] = null;
			sink(1);

			return min;
		} else {
			return null;
		}
	}

	private void sink(int k) {
		while (2 * k <= size) {
			int j = 2 * k;
			if (j < size && more(j, j + 1))
				++j;
			if (!more(k, j))
				break;
			exch(k, j);
			k = j;
		}
	}

	private void exch(int i, int j) {
		BinaryNode atI = get(data, i);
		BinaryNode atJ = get(data, j);

		atI.pos = j;
		atJ.pos = i;

		data[i] = atJ;
		data[j] = atI;
	}

	@Override
	public PQNode<T> insert(T element) {
		if (size == data.length - 1) {
			System.out.println("calling resize()");
			resize(2 * data.length);
		}

		BinaryNode newElement = new BinaryNode(element);
		newElement.pos = ++size;
		data[size] = newElement;
		swim(size);

		return newElement;
	}

	private boolean more(int i, int j) {
		return get(data, i).element.compareTo(get(data, j).element) > 0;
	}

	private void swim(int k) {
		while (k > 1 && more(k / 2, k)) {
			exch(k / 2, k);
			k = k / 2;
		}
	}

	@Override
	public int size() {
		return size;
	}

	private void decreaseKey(BinaryNode element) {
		swim(element.pos);
	}

	@Override
	public void decreaseKey(PQNode<T> element) {
		if (element instanceof BinaryHeap.BinaryNode) {
			decreaseKey((BinaryNode) element);
		}
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}
}
