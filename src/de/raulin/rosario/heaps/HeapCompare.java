package de.raulin.rosario.heaps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class HeapCompare {

	private final static Random RANDGEN = new Random();

	private static void test(PriorityQueue<Integer> pq, Integer[] testData,
			Set<Integer> toChangePrio) {
		List<PQNode<Integer>> changePrio = new ArrayList<PQNode<Integer>>();

		long insertStart = System.nanoTime();
		int pos = 0;
		for (Integer i : testData) {
			PQNode<Integer> e = pq.insert(i);
			if (toChangePrio.contains(pos)) {
				changePrio.add(e);
			}
			++pos;
		}
		long insertDuration = System.nanoTime() - insertStart;
		System.out.printf("insert() took %f seconds.\n",
				insertDuration / 1000000000.0);

		assert (pq.size() == testData.length);

		System.out.printf("Changing priority of %d elements.\n",
				changePrio.size());
		long decreaseStart = System.nanoTime();
		for (PQNode<Integer> cp : changePrio) {
			cp.element--;
			pq.decreaseKey(cp);
		}
		long decreaseDuration = System.nanoTime() - decreaseStart;
		System.out.printf("Changing priority took %f seconds.\n",
				decreaseDuration / 1000000000.0);

		Integer[] extracted = new Integer[pq.size()];
		int i = 0;

		long extractStart = System.nanoTime();
		while (!pq.isEmpty()) {
			extracted[i++] = pq.extractMin();
		}
		long extractDuration = System.nanoTime() - extractStart;
		System.out.printf("extractMin() took %f seconds.\n",
				extractDuration / 1000000000.0);

		assert (isSorted(extracted));
		// Arrays.sort(testData);
		// assert(Arrays.equals(extracted, testData));
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			Set<Integer> toChangePrio = new HashSet<Integer>();
			Integer testSize = Integer.parseInt(args[0]);
			Integer[] toInsert = new Integer[testSize];

			for (int i = 0; i < testSize; ++i) {
				toInsert[i] = RANDGEN.nextInt(testSize);

				if (RANDGEN.nextInt(100) % 7 == 0) {
					toChangePrio.add(i);
				}
			}

			Comparator<Integer> comp = new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return o1 - o2;
				}
				
			};
			
			PriorityQueue<Integer> fibHeap = new FibonacciHeap<Integer>(comp);
			PriorityQueue<Integer> binHeap = new BinaryHeap<Integer>(testSize, comp);

			assert (fibHeap.size() == 0);
			assert (binHeap.size() == 0);

			System.out.println("fibonacci heap:");
			test(fibHeap, toInsert, toChangePrio);
			System.out.println("binary heap:");
			test(binHeap, toInsert, toChangePrio);
		}
	}

	private static boolean isSorted(Integer[] data) {
		for (int i = 0; i < data.length - 1; ++i) {
			if (data[i] > data[i + 1])
				return false;
		}
		return true;
	}
}
