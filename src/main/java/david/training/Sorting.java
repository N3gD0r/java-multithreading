package david.training;

import java.util.concurrent.RecursiveAction;

public class Sorting extends RecursiveAction {
	private static final int THRESHOLD = 10000;
	private final int[] array;
	private final int low;
	private final int high;

	public Sorting(int[] array, int low, int high) {
		this.array = array;
		this.low = low;
		this.high = high;
	}

	@Override
	protected void compute() {
		if (high - low < THRESHOLD) {
			quickSort(array, low, high);
		} else {
			int pivotIndex = partition(array, low, high);
			Sorting left = new Sorting(array, low, pivotIndex - 1);
			Sorting right = new Sorting(array, pivotIndex + 1, high);
			invokeAll(left, right);
		}
	}

	private void quickSort(int[] arr, int low, int high) {
		if (low < high) {
			int pivotIndex = partition(arr, low, high);
			quickSort(arr, low, pivotIndex - 1);
			quickSort(arr, pivotIndex + 1, high);
		}
	}

	private int partition(int[] arr, int low, int high) {
		int pivot = arr[high];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (arr[j] <= pivot) {
				i++;
				swap(arr, i, j);
			}
		}
		swap(arr, i + 1, high);
		return i + 1;
	}

	private void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
}
