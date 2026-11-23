package concurrency;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private static final int READ_COUNT = 5;

    private final BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < READ_COUNT; i++) {
                String value = queue.take(); // thread will be blocked if the queue is empty
                System.out.println("Consumed: " + value);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
