package sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShellSort implements ISorter {

	private int[] gaps = {701, 301, 132, 57, 23, 10, 4, 1};

	private int comparisons = 0;

	/**
	 * Source: http://en.wikipedia.org/wiki/Shellsort
	 */
	@Override
	public List<Integer> sortAscending(List<Integer> list) {
		comparisons = 0;
		int n = list.size();

		// Start with the largest gap and work down to a gap of 1
		for (int gap : gaps) {
			// Do a gapped insertion sort for this gap size.
			// The first gap elements a[0..gap-1] are already in gapped order
			// keep adding one more element until the entire array is gap sorted
			for (int i = gap; i < n; ++i) {
				// add a[i] to the elements that have been gap sorted
				// save a[i] in temp and make a hole at position i
				int temp = list.get(i);
				// shift earlier gap-sorted elements up until the correct
				// location for a[i] is found
				int j;
				for (j = i; j >= gap; j -= gap) {
					comparisons++;
					if (list.get(j - gap) > temp) {
						list.set(j, list.get(j - gap));
						continue;
					}
					break;
				}
				// put temp (the original a[i]) in its correct location
				list.set(j, temp);
			}
		}
		return list;
	}

	@Override
	public int getNumberOfComparisons() {
		return comparisons;
	}

	public static void main(String[] args) {
		ISorter sorter = new ShellSort();
		Random r = new Random();
		ArrayList<Integer> l = new ArrayList<>();
		for (int i = 0; i < 10000; ++i) {
			l.add(r.nextInt(100));
		}
		sorter.sortAscending(l);
		System.out.println("is sorted? " + isSorted(l));
		System.out.println("Number of comparisons: " + sorter.getNumberOfComparisons());
// for (int i : l) {
// System.out.print(i + " ");
// }
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
