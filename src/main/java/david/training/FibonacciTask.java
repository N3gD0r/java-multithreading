package david.training;

import java.util.concurrent.RecursiveTask;

public class FibonacciTask extends RecursiveTask<Long> {
	private static final long MIN_PARALLEL_NUMBER = 10;
	private final int number;

	FibonacciTask(int number) {
		this.number = number;
	}

	@Override
	protected Long compute() {
		if (number <= MIN_PARALLEL_NUMBER) {
			return fibLoop(number);
		}
		FibonacciTask forkTask = new FibonacciTask(number - 1);
		forkTask.fork();
		FibonacciTask mainTask = new FibonacciTask(number - 2);
		return mainTask.compute() + forkTask.join();
	}

	static long fibLoop(int n) {
		long result = 1;
		long prev = 0;
		for (int i = 1; i < n; i++) {
			long temp = result;
			result += prev;
			prev = temp;
		}
		return result;
	}
}
