package david.training;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class Factorial extends RecursiveTask<BigInteger> {
	private BigInteger start;
	private BigInteger end;
	private static final BigInteger THRESHOLD = BigInteger.valueOf(10);

	public Factorial(BigInteger start, BigInteger end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected BigInteger compute() {
		BigInteger length = end.subtract(start.add(BigInteger.ONE));
		if (length.compareTo(THRESHOLD) <= 0) {
			BigInteger result = BigInteger.ONE;
			for (BigInteger i = start; i.compareTo(end) <= 0; i = i.add(BigInteger.ONE)) {
				result = result.multiply(i);
			}
			return result;
		} else {
			BigInteger mid = length.divide(BigInteger.valueOf(2));
			Factorial firstTask = new Factorial(start, start.add(mid));
			Factorial secondTask = new Factorial(start.add(mid).add(BigInteger.ONE), end);
			firstTask.fork();
			return secondTask.compute().multiply(firstTask.join());
		}
	}
}
