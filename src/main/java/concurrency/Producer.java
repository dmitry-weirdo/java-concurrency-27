package concurrency;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<String> queue;

    private static final int TIMEOUT = 500;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // put will wait if the queue is full

            queue.put("A");
            Thread.sleep(TIMEOUT);

            queue.put("B");
            Thread.sleep(TIMEOUT);

            queue.put("C");
            Thread.sleep(TIMEOUT);

            queue.put("D");
            Thread.sleep(TIMEOUT);

            queue.put("E");
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


