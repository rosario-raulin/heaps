package de.raulin.rosario.heaps;

/**
 * A priority queue based on T implementing Comparable on T.
 * <p>
 * This interface represents a min-priority-queue, i. e. for any
 * two elements e1 and e2 if e1.compareTo(e2) < 0, e1 will be first.
 * 
 * @author Rosario Raulin
 * @param <T> the type of object being stored
 */
public interface PriorityQueue<T> {
	/**
	 *  Returns the smallest element in this priority queue without
	 *  modifying the queue. That is, two successive calls to this
	 *  method will return the same element unless extractMin() or
	 *  decreaseKey() is called.
	 *  
	 *  @see #extractMin
	 *  @see #decreaseKey
	 *	@return the smallest element or null, if queue is empty
	 */
	public T min();
	
	/**
	 * Return the smallest element in this priority queue and
	 * removes it. The next call to {@link #extractMin()} or {@link #min()}
	 * will NOT return the same element but the second smallest element
	 * in this priority queue.
	 * 
	 * @return the smallest element or null, if queue is empty
	 */
	public T extractMin();
	
	/**
	 * Inserts the element and rearranges the queue to maintain the order.
	 * 
	 * @param element the element to be inserted
	 * @return a handle needed to use {@link #decreaseKey(PQNode)}.
	 */
	public PQNode<T> insert(T element);
	
	/**
	 * Returns the size (number of element) of the queue.
	 * 
	 * @return the number of elements in the queue
	 */
	public int size();
	
	/**
	 * Rearranges the queue after element's priority decreased.
	 * <p>
	 * Note: This method does not work if element's priority increased!
	 * 
	 * @param element the element with decreased priority
	 */
	public void decreaseKey(PQNode<T> element);
	
	/**
	 * Returns true if the priority queue doesn't contain any elements.
	 * 
	 * @return true if priority queue is empty
	 */
	public boolean isEmpty();
}
