package sorting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CountingSort implements ISorter {

	private int numberOfComparisons;

	@Override
	public List<Integer> sortAscending(List<Integer> list) {

		numberOfComparisons = 0;

		// check for empty list
		if (list.size() == 0) {
			return list;
		}

		// find min and max value of list
		int min = list.get(0);
		int max = list.get(0);
		for (int number : list) {
			min = Math.min(min, number);
			max = Math.max(max, number);
			numberOfComparisons += 2;
		}

		// init counting array
		int[] counters = new int[max - min + 1];

		// count list items
		for (int number : list) {
			counters[number - min]++;
		}

		// calculate the starting index for each key
		int total = 0;
		for (int i = 0; i < counters.length; ++i) {
			int oldCount = counters[i];
			counters[i] = total;
			total += oldCount;
		}

		// create list for sorted output
		List<Integer> sorted = new ArrayList<>(list);

		// copy to output, preserving order of inputs with equal keys
		for (int number : list) {
			sorted.set(counters[number-min]++, number);
		}
		return sorted;
	}

	@Override
	public int getNumberOfComparisons() {
		return numberOfComparisons;
	}

	public static void main(String[] args) {
		ISorter sorter = new CountingSort();
		Random r = new Random();
		List<Integer> l = new LinkedList<>();
		for (int i = 0; i < 10; ++i) {
			l.add(r.nextInt(600)-11);
		}
		l = sorter.sortAscending(l);
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
