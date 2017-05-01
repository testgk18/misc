package sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionSort implements ISorter {

	private int comparisons = 0;

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		for (int j = 0; j < 50; ++j) {
			list.add((int)(Math.random() * 199 - 99));
		}
		ISorter sorter = new SelectionSort();
		
		sorter.sortAscending(list);
		System.out.println(list);
	}

	@Override
	public List<Integer> sortAscending(List<Integer> list) {
		comparisons = 0;
		for (int i = 1; i < list.size(); ++i) {
			int minIndex = i - 1;
			for (int j = i; j < list.size(); ++j) {
				comparisons++;
				if (list.get(j) < list.get(minIndex)) {
					minIndex = j;
				}
			}
			Collections.swap(list, i - 1, minIndex);
		}
		return list;
	}
	
	@Override
	public int getNumberOfComparisons() {
		return comparisons;
	}

}
