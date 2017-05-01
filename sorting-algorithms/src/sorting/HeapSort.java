package sorting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HeapSort implements ISorter {

	private int comparisons;

	@Override
	public List<Integer> sortAscending(List<Integer> list) {
		comparisons = 0;
		heapify(list);
		for (int end = list.size() - 1; end > 0; end--) {
			Collections.swap(list, end, 0);
			siftDown(list, 0, end);
		}
		return list;
	}

	@Override
	public int getNumberOfComparisons() {
		return comparisons;
	}

	private void heapify(List<Integer> numbers) {
		for (int i = (numbers.size()) / 2; i >= 0; --i) {
			siftDown(numbers, i, numbers.size());
		}
	}

	private void siftDown(List<Integer> numbers, int parent, int size) {
		assert parent >= 0;
		int child = parent * 2 + 1;
		assert child > parent;
		while (child < size) {
			int swap = parent;
			comparisons++;
			if (numbers.get(parent) < numbers.get(child)) {
				swap = child;
			}
			child++;
			comparisons++;
			if (child < size && numbers.get(child) > numbers.get(swap)) {
				swap = child;
			}
			if (swap == parent) {
				return;
			}

			Collections.swap(numbers, parent, swap);
			parent = swap;

			child = swap * 2 + 1;
		}
	}

	public static void main(String[] args) {
		ISorter sorter = new HeapSort();
		Random r = new Random();
		List<Integer> l = new LinkedList<>();
		for (int i = 0; i < 25; ++i) {
			l.add(r.nextInt(10));
		}
		sorter.sortAscending(l);
		System.out.println("is sorted? " + isSorted(l));
		System.out.println("Number of comparisons: " + sorter.getNumberOfComparisons());
		System.out.println(l);
	}

	static boolean isSorted(List<Integer> list) {
		for (int i = 1; i < list.size(); ++i) {
			if (list.get(i - 1) > list.get(i)) {
				return false;
			}
		}
		return true;
	}

}
