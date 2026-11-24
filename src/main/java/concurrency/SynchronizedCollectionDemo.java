package concurrency;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SynchronizedCollectionDemo {

    private static final int ITERATIONS = 1000;

        private static final List<Integer> list = Collections.synchronizedList(new ArrayList<>());
//    private static final List<Integer> list = new CopyOnWriteArrayList<>(); // memory-expensive, but can be quicker than synchronizedList

    private static final Object monitor = new Object();

    private static class IntegerGenerator implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                synchronized (monitor) {
                    list.add(new Random().nextInt(10));

                    // will throw ConcurrentModificationException if not synchronized
                    // will work on CopyOnWriteArrayList without synchronization
                    print(list);
                }
            }
        }
    }

    static void main() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
            IntegerGenerator task = new IntegerGenerator();
            Thread thread = new Thread(task);

            threads.add(thread);
        }

        System.out.println("Starting threads...");

        for (Thread thread : threads) {
            thread.start();
        }

        System.out.println("Waiting for threads to finish...");

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("List size: " + list.size());
        System.out.println("List values: " + list.toString());
    }

    private static void print(Collection<?> c) {
        StringBuilder sb = new StringBuilder();

        Iterator<?> iterator = c.iterator();

        // this can throw ConcurrentModificationException, even on a synchronized list
        while (iterator.hasNext()) {
            sb
                    .append(iterator.next())
                    .append(" ");
        }
    }
}
