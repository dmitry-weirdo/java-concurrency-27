package concurrency;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorsDemo {

    private static class IntegerGenerator implements Runnable {
        @Override
        public void run() {
            int number = new Random().nextInt(10);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Thread has finished. Number: " + number);
        }
    }

    static void main() {
        LocalDateTime start = LocalDateTime.now();

        List<Future<?>> results = new ArrayList<>();

        int numberOfThreads = 10;

        System.out.println("Number of threads used: " + numberOfThreads);

//        ExecutorService executorService = Executors.newSingleThreadExecutor(); // single working thread
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads); // parallelize the threads

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();  // yes, we can use virtual threads in Executors

        int totalThreads = 10;
        for (int i = 0; i < totalThreads; i++) {
            IntegerGenerator integerGenerator = new IntegerGenerator();
            Future<?> future = executorService.submit(integerGenerator);

            results.add(future);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Results are done: ");
        for (Future<?> result : results) {
            System.out.print(result.isDone() + " ");
        }

        System.out.println();

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Results are done AFTER executorService.awaitTermination: ");
        for (Future<?> result : results) {
            System.out.print(result.isDone() + " ");
        }

        System.out.println();

        System.out.printf(
                "Time of work: %s milliseconds %n",
                Duration.between(start, LocalDateTime.now()).getNano() / 1_000_000
        );
    }

}
