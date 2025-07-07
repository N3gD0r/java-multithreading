package david.training.semaphores;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Consumer extends Thread {
    private final Semaphore semaphore;
    private final Queue<Integer> sharedQueue;

    public Consumer(Semaphore semaphore, Queue<Integer> sharedQueue) {
        this.semaphore = semaphore;
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        int item = 0;
        while (item != Integer.MIN_VALUE) {
            try {
                semaphore.acquire();
            } catch (InterruptedException ignored) {
            }
            synchronized (sharedQueue) {
                item = Optional.ofNullable(sharedQueue.poll()).orElse(0);
            }
            System.out.printf("%n%s consumed %d (%d)%n", getName(), item, semaphore.availablePermits());
        }
    }
}
