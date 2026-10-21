package david.training.filestats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileScanner {

	private static class FileScannerTask extends RecursiveTask<FileStat> {
		private final File root;
		private final AtomicBoolean cancelled;

		public FileScannerTask(File root, AtomicBoolean cancelled) {
			this.root = root;
			this.cancelled = cancelled;
		}

		@Override
		protected FileStat compute() {
			if (cancelled.get()) {
				return new FileStat();
			}

			if (root.isFile()) {
				FileStat stats = new FileStat();
				stats.totalFiles++;
				stats.fileSize += root.length();
				return stats;
			}

			File[] children = root.listFiles();

			if (children == null) {
				return new FileStat();
			}

			FileStat stats = new FileStat();
			List<FileScannerTask> tasks = new ArrayList<>();

			for (File child : children) {
				if (cancelled.get()) {
					break;
				}
				if (child.isFile()) {
					stats.totalFiles++;
					stats.fileSize += child.length();
					continue;
				}
				if (child.isDirectory()) {
					FileScannerTask task = new FileScannerTask(child, cancelled);
					task.fork();
					tasks.add(task);
					stats.totalFolders++;
				}
			}

			for (FileScannerTask task : tasks) {
				stats.merge(task.join());
			}
			return stats;
		}
	}

	public static FileStat scanOf(File file, AtomicBoolean cancelled) throws InterruptedException {
		final ForkJoinPool pool = new ForkJoinPool();
		try {
			FileScannerTask scannerTask = new FileScannerTask(file, cancelled);

			Thread cancelListener = new Thread(() -> {
				System.out.println("Press 'c' to cancel.");
				while (!cancelled.get() && !scannerTask.isDone()) {
					try {
						if (System.in.available() > 0) {
							int ch = System.in.read();
							if (ch == 'c' || ch == 'C') {
								cancelled.set(true);
								System.out.println("\nCancelling scan...");
								break;
							}
						}
					} catch (IOException ignored) {
					}
				}
			});

			Thread scanAnimation = new Thread(() -> {
				String[] frameChars = { "|", "/", "-", "\\", "|", "/", "-", "\\" };
				int index = 0;
				while (!scannerTask.isDone() && !cancelled.get()) {
					System.out.print("\rScanning " + frameChars[index++ % frameChars.length]);
					try {
						Thread.sleep(200);
					} catch (InterruptedException ignored) {
					}
				}
			});

			cancelListener.start();
			scanAnimation.start();

			FileStat fileStats = pool.invoke(scannerTask);

			scanAnimation.join();
			cancelListener.interrupt();

			return fileStats;
		} finally {
			pool.shutdown();
		}
	}
}
