package asýnc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class GetQuotationCompletableFuture {

    static void main() {
        Quotation.debug = true;

        var start = Instant.now();

        Supplier<Quotation> readFromA = Quotation::readFromA;
        Supplier<Quotation> readFromB = Quotation::readFromB;
        Supplier<Quotation> readFromC = Quotation::readFromC;

        // will execute on ForkJoinPool threads
        // will run concurrently
        CompletableFuture<Quotation> cfa = CompletableFuture.supplyAsync(readFromA);
        CompletableFuture<Quotation> cfb = CompletableFuture.supplyAsync(readFromB);
        CompletableFuture<Quotation> cfc = CompletableFuture.supplyAsync(readFromC);

        var doneSupplying = Instant.now();

        IO.println("Duration of supplying: " + Duration.between(start, doneSupplying).toMillis() + " ms");

        // CompletableFuture.join is blocking the main thread
        IO.println(cfa.join());
        IO.println(cfb.join());
        IO.println(cfc.join());

        var end = Instant.now();
        IO.println("Duration from supplying to getting result: " + Duration.between(doneSupplying, end).toMillis() + " ms");
    }
}
