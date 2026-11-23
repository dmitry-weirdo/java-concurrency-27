package concurrency;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CallableDemo {

    private static final StringBuffer sb = new StringBuffer();

    private static void log(String s) {
        sb.append(s + "\n");
//        sb.append(s).append("\n"); // this can cause splitting two .append() calls between 2 threads
    }

    private static class IntegerGenerator implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int number = new Random().nextInt(10); // from 0 to 9

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Thread has finished. Number: " + number);

            log("Thread has finished. Number: " + number);

            return number;
        }
    }


    static void main() {
        LocalDateTime start = LocalDateTime.now();

        List<Future<Integer>> results = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        int totalThreads = 100;
        for (int i = 0; i < totalThreads; i++) {
            IntegerGenerator integerGenerator = new IntegerGenerator();
            Future<Integer> future = executorService.submit(integerGenerator);

            results.add(future);
        }


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Results: ");
        for (Future<Integer> result : results) {
            if (!result.isDone()) {
                result.cancel(true); // interrupt running
                continue;
            }

            try {
                System.out.print(result.get() + " ");
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

//            System.out.print(result.isDone() + " ");
        }

        System.out.println();

        executorService.shutdown();

        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
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
