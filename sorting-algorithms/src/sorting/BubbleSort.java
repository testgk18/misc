package sorting;

public class BubbleSort {

	public static void main(String[] args) {

		int[] numbers = new int[args.length];
		for (int i = 0; i < numbers.length; ++i) {
			numbers[i] = Integer.parseInt(args[i]);
		}
		sort(numbers);
		for (int n : numbers) {
			System.out.print(n + " ");
		}
		System.out.println();
	}

	static void sort(int[] array) {
		int lower = 0;
		for (boolean flag = true; flag; lower++) {
			flag = false;
			for (int i = array.length-1; i > lower; --i) {
				if (array[i - 1] < array[i]) {
					flag = true;
					swap(array, i - 1, i);
				}
			}
		}
	}

	static void swap(int[] array, int index1, int index2) {
		int t = array[index1];
		array[index1] = array[index2];
		array[index2] = t;
		System.out.println("swap: " + index1 + ", " + index2);
	}

}
