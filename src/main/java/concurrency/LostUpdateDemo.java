package concurrency;

public class LostUpdateDemo {

    private static class SynchronizedThread implements Runnable {

        private int value = 0;

        @Override
        public void run() {

            for (int i = 0; i < 5000; i++) {
                increment();

                Thread.yield(); // current thread is willing to yield its current use of the processor
            }
        }

        public synchronized void increment() { // synchronized requires to acquire a lock
            // critical section - accesses shared resources

            // non-atomic!!!
            int i = value;
            value = i + 1;
        }

        public int getValue() {
            return value;
        }
    }

    static void main() {
        SynchronizedThread job = new SynchronizedThread();

        Thread t1 = new Thread(job, "t1");
        Thread t2 = new Thread(job, "t2");

        t1.start();
        t2.start();

        // wait for the execution of 2 threads
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Job value: " + job.getValue());
    }
}
