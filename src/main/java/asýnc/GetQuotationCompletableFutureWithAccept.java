package asýnc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class GetQuotationCompletableFutureWithAccept {

    static void main() {
        Quotation.debug = true;

        var start = Instant.now();

        Supplier<Quotation> readFromA = Quotation::readFromA;
        Supplier<Quotation> readFromB = Quotation::readFromB;
        Supplier<Quotation> readFromC = Quotation::readFromC;

        // will execute on ForkJoinPool threads
        // will run concurrently
        // thenAccept returns CompletableFuture<Void>
        CompletableFuture<Void> cfa = CompletableFuture.supplyAsync(readFromA).thenAccept(IO::println);
        CompletableFuture<Void> cfb = CompletableFuture.supplyAsync(readFromB).thenAccept(IO::println);
        CompletableFuture<Void> cfc = CompletableFuture.supplyAsync(readFromC).thenAccept(IO::println);

        var doneSupplying = Instant.now();
        IO.println("Duration of supplying: " + Duration.between(start, doneSupplying).toMillis() + " ms");

        // CompletableFuture.join is blocking the main thread
        cfa.join();
        cfb.join();
        cfc.join();

        var end = Instant.now();
        IO.println("Duration from supplying to getting result: " + Duration.between(doneSupplying, end).toMillis() + " ms");
    }
}
