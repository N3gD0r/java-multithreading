package david.training.semaphores;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Producer extends Thread {
    private static final int AMOUNT = 100;
    private final Semaphore semaphore;
    private final Queue<Integer> sharedQueue;

    public Producer(Semaphore semaphore, Queue<Integer> sharedQueue) {
        this.semaphore = semaphore;
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < AMOUNT; i++) {
            synchronized (sharedQueue) {
                sharedQueue.offer(i);
            }
            semaphore.release();
            System.out.printf("%n%s produced %d (%d)%n", getName(), i, semaphore.availablePermits());
        }
        synchronized (sharedQueue) {
            sharedQueue.offer(Integer.MIN_VALUE);
        }
        semaphore.release();
    }
}
