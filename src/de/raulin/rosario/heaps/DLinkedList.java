package de.raulin.rosario.heaps;

import java.util.Iterator;

/**
 * A circular, doubly-linked list that supports constant time operations only
 * (except for the iterator, of course).
 * 
 * @author Rosario Raulin
 * @param <T> The element's type.
 */
public class DLinkedList<T> implements Iterable<T> {
	
	/**
	 * A class representing the internal nodes of a circular, doubly-linked list.
	 * 
	 * @author Rosario Raulin
	 */
	public class DNode {
		private T data;
		private DNode prev;
		private DNode next;
		
		/**
		 * Creates a DNode storing the data and having a predecessor
		 * and successor.
		 * 
		 * @param data the actual object being stored
		 * @param prev the predecessor of this
		 * @param next the successor of this
		 */
		public DNode(T data, DNode prev, DNode next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}
		
		/**
		 * Creates a DNode storing storing data and having no predecessor
		 * and no successor.
		 * 
		 * @param data the actual object being stored
		 */
		public DNode(T data) {
			this(data, null, null);
		}
		
		/**
		 * Returns the predecessor of this node.
		 *  
		 * @return the predecessor or null, if none exists
		 */
		public DNode getPrev() {
			return prev;
		}
		
		/**
		 * Returns the successor of this node.
		 * 
		 * @return the successor or null, if none exits
		 */
		public DNode getNext() {
			return next;
		}
		
		/**
		 * Returns the actual data stored in this node
		 * 
		 * @return the actual data
		 */
		public T getData() {
			return data;
		}
	}
	
	private int size;
	private DNode head;
	
	/**
	 * Creates an empty list.
	 */
	public DLinkedList() {
		this.size = 0;
		this.head = null;
	}
	
	/**
	 * Inserts the element at the end of the list. Its guaranteed runtime is O(1).
	 * 
	 * @param element the element to be inserted
	 * @return a reference to the node representing the element
	 */
	public DNode push_back(T element) {
		DNode next = new DNode(element);
		if (head != null) {
			next.prev = head.prev;
			next.next = head;
			if (head.prev != null) {
				head.prev.next = next;
			}
			head.prev = next;
		} else {
			next.prev = next;
			next.next = next;
			head = next;
		}
		++size;
		return next;
	}
	
	/**
	 * Inserts the element at the front of the list. Its guaranteed runtime is O(1).
	 * 
	 * @param element the element to be inserted
	 * @return a reference to the node representing the element
	 */
	public DNode push_front(T element) {
		DNode next = push_back(element);
		head = next;
		return next;
	}
	
	/**
	 * Removes the element represented by node. Its guaranteed runtime is O(1).
	 * <p>
	 * Note: The node must be present in the list.
	 * 
	 * @param node the node to be deleted
	 */
	public void delete(DNode node) {
		--size;
		DNode prev = node.prev;
		DNode next = node.next;
		
		if (node == next) { // node is the last element of the list
			head = null;
		} else {
			prev.next = next;
			next.prev = prev;
			if (node == head) {
				head = next;
			}
		}
//		// we help the garbage collector by setting next and prev to null
//		node.next = null;
//		node.prev = null;
	}
	
	/**
	 * Returns the size (number of elements) in the list.
	 * @return the number of elements in the list
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Appends the other list to this one. Its guaranteed runtime is O(1).
	 * <p>
	 * Note: This method destroys other in that it changes it's internal structure.
	 * This way we can guarantee constant runtime.
	 * 
	 * @param other the list to be appended to this one
	 */
	public void append(DLinkedList<T> other) {
		if (size == 0) {
			size = other.size;
			head = other.head;
		} else if (other.size > 0) {
			size += other.size;
			DNode last = head.prev;
			DNode olast = other.head.prev;
			
			last.next = other.head;
			other.head.prev = last;
			olast.next = head;
			head.prev = olast;
		}
	}

	/**
	 * Returns a forward-iterator starting at this list's head and ending at head.prev.
	 * <p>
	 * Note: The iterator doesn't support remove().
	 * 
	 * @return a forward-iterator for the list 
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int stopAfter = size;
			private int round = 0;
			private DNode curr = head;
			
			@Override
			public boolean hasNext() {
				return round < stopAfter;
			}

			@Override
			public T next() {
				++round;
				T data = curr.data;
				curr = curr.next;
				return data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
	public static void main(String[] args) {
		DLinkedList<Integer> lst = new DLinkedList<Integer>();
		
		DLinkedList<Integer>.DNode remove = lst.push_back(23);
		lst.push_back(42);
		
		lst.delete(remove);
		
		for (Integer i : lst) {
			System.out.println(i);
		}
		
	}
}
