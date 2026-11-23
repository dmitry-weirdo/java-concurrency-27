package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterDemo {

    private static final int ITERATIONS_COUNT = 1_000_000;
    private static final int THREADS_COUNT = 100;

//    private static int counter = 0;
    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final Object lock = new Object();

    private static class ThreadExample implements Runnable {
        private String name;

        public ThreadExample(String name) {
            this.name = name;
        }


        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS_COUNT; i++) {
//                counter++; // non-atomic operation on int primitive!

/*
                synchronized (lock) { // slow, but is working
                    counter++; // non-atomic operation on int primitive!
                }
*/

                // AtomicInteger is fast, uses the processor-level compare-and-swap operation!
                counter.getAndIncrement();
            }
        }
    }

    static void main() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREADS_COUNT; i++) {
            ThreadExample task = new ThreadExample("t" + i);
            Thread thread = new Thread(task);

            threads.add(thread);
        }

        System.out.println("Starting threads...");

        for (Thread thread : threads) {
            thread.start();
        }

        // wait for execution of all the threads

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Counter: " + counter);
    }
}
