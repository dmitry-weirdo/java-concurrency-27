package asýnc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

public class GetQuotationConcurrent {

    static void main() {
        Quotation.debug = true;

        var start = Instant.now();

        var threadsCount = 10;

        // ExecutorService is AutoCloseable and can be used in try-with-resources
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadsCount)) {
            Callable<Quotation> readFromA = Quotation::readFromA;
            Callable<Quotation> readFromB = Quotation::readFromB;
            Callable<Quotation> readFromC = Quotation::readFromC;

            var futureA = executorService.submit(readFromA);
            var futureB = executorService.submit(readFromB);
            var futureC = executorService.submit(readFromC);

            // !!! future.get() is blocking, so this (main) thread will be blocked
            // However, the threads themselves can be parallelized
            try {
                var quotationA = futureA.get();
                var quotationB = futureB.get();
                var quotationC = futureC.get();

                IO.println(quotationA);
                IO.println(quotationB);
                IO.println(quotationC);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        var end = Instant.now();
        IO.println("Duration: " + Duration.between(start, end).toMillis() + " ms");
    }
}
