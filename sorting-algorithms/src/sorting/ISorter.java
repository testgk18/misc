package sorting;

import java.util.List;

public interface ISorter {
	
	public List<Integer> sortAscending(List<Integer> list);

	public int getNumberOfComparisons();

}
