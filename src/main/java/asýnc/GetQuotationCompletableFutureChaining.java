package asýnc;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class GetQuotationCompletableFutureChaining {

    static void main() {
        Quotation.debug = true;

        var start = Instant.now();

        Supplier<Quotation> readFromA = Quotation::readFromA;
        Supplier<Quotation> readFromB = Quotation::readFromB;
        Supplier<Quotation> readFromC = Quotation::readFromC;

        var jobs = List.of(readFromA, readFromB, readFromC);


       var cfs = jobs
                .stream() // !!! NO parallel
                .map(CompletableFuture::supplyAsync)
                .toArray(CompletableFuture[]::new); // use array instead of list, to use as vararg

        var doneSupplying = Instant.now();
        IO.println("Duration of supplying: " + Duration.between(start, doneSupplying).toMillis() + " ms");

        CompletableFuture<Void> chainCf = CompletableFuture
                .allOf(cfs) // wait for all the CF from the array to complete
                .thenApply(_ -> Arrays
                        .stream(cfs)
                        .map(CompletableFuture::join)
                        .map(Quotation.class::cast)
                        .min(Comparator.comparing(Quotation::price))
                        .orElseThrow()
                )
                .thenApply(
                        EmailService::sendEmail
                )
                .thenAccept(
                        DBService::log
                );


        // wait for the complete chain to complete
        chainCf.join();

//        IO.println("Results: " + results);

        var end = Instant.now();
        IO.println("Duration from supplying to handling the chain: " + Duration.between(doneSupplying, end).toMillis() + " ms");
    }
}
