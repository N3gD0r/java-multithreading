package david.training;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

import david.training.employees.TaskEmployee;
import david.training.filestats.FileScanner;
import david.training.filestats.FileStat;
import david.training.semaphores.Solution;

import javax.imageio.ImageIO;

public class App {
	static final AtomicBoolean cancelled = new AtomicBoolean(false);

	public static void main(String[] args) {
		System.out.println("1. FACTORIAL VIA FJP");
		System.out.println("########################################");
		long startTime = System.nanoTime();
		int n = 100;
		BigInteger seqResult = BigInteger.ONE;
		for (int i = 2; i <= n; i++) {
			seqResult = seqResult.multiply(BigInteger.valueOf(i));
		}
		long finishTime = System.nanoTime();
		long elapsedTime = finishTime - startTime;
		System.out.println("Sequential result: " + seqResult);
		System.out.println("Sequential time: " + elapsedTime + "ns");

		startTime = System.nanoTime();
		Factorial factorial = new Factorial(BigInteger.ONE, BigInteger.valueOf(n));
		ForkJoinPool pool = ForkJoinPool.commonPool();
		BigInteger result = pool.invoke(factorial);
		finishTime = System.nanoTime();
		elapsedTime = finishTime - startTime;
		System.out.println("FJP result: " + result);
		System.out.println("FJP time: " + elapsedTime + "ns");
		System.out.println("########################################");

		System.out.println("\n2. MULTITHREADING SORTING VIA FJP");
		System.out.println("########################################");
		int size = 100;
		int[] array = new Random().ints(size).toArray();
		System.out.println("Before sorting:");
		System.out.println(Arrays.toString(array));

		Sorting sorting = new Sorting(array, 0, array.length - 1);
		pool.invoke(sorting);
		System.out.println("After sorting:");
		System.out.println(Arrays.toString(array));
		System.out.println("########################################");

		System.out.println("\n3. File Scanner via FJP");
		System.out.println("########################################");
		String path = ".";
		System.out.print("Enter path to scan: ");
		try (Scanner input = new Scanner(System.in)) {
//			path = input.nextLine();

			File filePath = new File(path);
			if (!filePath.exists()) {
				System.err.println("\nInvalid path.");
				return;
			}
			FileStat fileStats = null;
			try {
				fileStats = FileScanner.scanOf(filePath, cancelled);
			} catch (InterruptedException ignored) {
			}
			if (fileStats == null) {
				System.err.println("\nSomething went wrong.");
				return;
			}
			System.out.printf("\nFolders: %d%n", fileStats.totalFolders);
			System.out.printf("Files: %d%n", fileStats.totalFiles);
			System.out.printf("Files: %d bytes%n", fileStats.fileSize);
		}

		System.out.println("########################################");

		System.out.println("\n4. COMPLETABLE FUTURE HELPS TO BUILD OPEN SALARY SOCIETY");
		System.out.println("########################################");
		TaskEmployee.solve();
		System.out.println("########################################");

		System.out.println("\n5. PRODUCER-CONSUMER PROBLEM");
		System.out.println("########################################");
		Solution.solve();
		System.out.println("########################################");

		System.out.println("\n6. RECURSIVE TASK AND RECURSIVE ACTION");
		System.out.println("########################################");

		long resultFibonacci = pool.invoke(new FibonacciTask(45));
		System.out.printf("Fibonacci number at position 45: %d%n", resultFibonacci);

		int sumOfSquaresSize = 500_000_000;
		double[] sumOfSquaresArray = new Random().doubles(sumOfSquaresSize, 1, 1001).toArray();

		SumOfSquaresTask sumOfSquaresTask = new SumOfSquaresTask(sumOfSquaresArray);
		pool.invoke(sumOfSquaresTask);
		double sumOfSquares = sumOfSquaresTask.getResult();

		sumOfSquares = 0;
		for (double v: sumOfSquaresArray){
			sumOfSquares += v * v;
		}

		System.out.printf("Sum of squares result: %f%n", sumOfSquares);
		System.out.printf("Sum of squares (sequential) result: %f%n", sumOfSquares);

		System.out.println("########################################");

		System.out.println("\n7. BLURRING FOR CLARITY");
		System.out.println("########################################");
		URL srcImage = App.class.getResource("red-tulips.jpg");
		if (srcImage == null) {
			throw new RuntimeException("Unable to find image");
		}
		BufferedImage image;
        try {
            image = ImageIO.read(srcImage);
        } catch (IOException e) {
			throw new RuntimeException(e);
        }
		if (image == null) {
			throw new RuntimeException("Image cannot be null");
		}
		BufferedImage blurredImage = Blurring.blur(image);
		String dstName = "blurred-tulips.jpg";
		File dstFile = new File(dstName);
        try {
            ImageIO.write(blurredImage, "jpg", dstFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		System.out.println("Output blurred image: " + dstName);
        System.out.println("########################################");
	}
}
