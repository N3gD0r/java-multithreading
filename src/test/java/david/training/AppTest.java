package david.training;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ForkJoinPool;

import org.junit.jupiter.api.Test;

public class AppTest {

	@Test
	public void shouldFibonacciRecursiveTaskEquals() {
		ForkJoinPool pool = new ForkJoinPool();
		FibonacciTask fibonacciTask = new FibonacciTask(45);
		long expected = 1134903170L;
		long result;
		try {
			result = pool.invoke(fibonacciTask);
		} finally {
			pool.shutdown();
		}
		assertEquals(expected, result);
	}
}
