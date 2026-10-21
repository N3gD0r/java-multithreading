package david.training.semaphores;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Solution {
    public static void solve() {
        Semaphore semaphore = new Semaphore(10);
        Queue<Integer> sharedQueue = new LinkedList<>();

        Producer producer = new Producer(semaphore, sharedQueue);
        Consumer consumer = new Consumer(semaphore, sharedQueue);
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException ignored) {
        }
    }
}
