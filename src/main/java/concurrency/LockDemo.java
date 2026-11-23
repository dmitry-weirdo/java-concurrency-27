package concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockDemo {

    private static Thread t1, t2, t3;

    private static final StringBuilder sb = new StringBuilder(); // !!! StringBuilder, NOT StringBuffer
    private static final StringBuilder result = new StringBuilder();

    private static final long ITERATIONS = 10_000; // count or writing iterations

    private static void log(String s) {
        result.append(s + "\n");
//        result.append(s).append("\n"); // this can cause splitting two .append() calls between 2 threads
    }

    private static class WritingThread implements Runnable {
        private final String name;
        private final Lock lock;

        public WritingThread(String name, Lock lock) {
            this.name = name;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                lock.lock(); // if lock is unavailable, the thread will sleep

                sb.append(name);
                Thread.yield();

                sb.append(name);
                Thread.yield();

                sb.append(" ");
                Thread.yield();

                lock.unlock();
            }
        }
    }

    private static class ReadingThread implements Runnable {
        private final Lock lock; // separate lock from the WritingThread

        public ReadingThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock(); // if lock is unavailable, the thread will sleep

                String s = sb.toString();

                // append the sb value to result, cut to last 30 symbols
                log(s.substring(Math.max(s.length() - 30, 0)).trim());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                lock.unlock();
                Thread.yield();
            }
        }
    }

    static void main() {
/*
        // using 2 separate ReentrantLock for read and write
        Lock writingLock = new ReentrantLock(); // can use a fair ordering policy or not, defaults to false
        Lock readingLock = new ReentrantLock(); // can use a fair ordering policy or not, defaults to false

        // thanks to using the same lock, it will always be AA or BB records, never AB or BA
        t1 = new Thread(new WritingThread("A", writingLock));
        t2 = new Thread(new WritingThread("B", writingLock));
        t3 = new Thread(new ReadingThread(readingLock));
*/

        // using a single
        ReadWriteLock lock = new ReentrantReadWriteLock(); // has 2 locks insides: readLock, writeLock

        // thanks to using the same lock, it will always be AA or BB records, never AB or BA
        t1 = new Thread(new WritingThread("A", lock.writeLock()));
        t2 = new Thread(new WritingThread("B", lock.writeLock()));
        t3 = new Thread(new ReadingThread(lock.readLock()));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Result: \n" + result);
    }
}
