package asýnc;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class GetQuotationCompletableFutureInStream {

    static void main() {
        Quotation.debug = true;

        var start = Instant.now();

        Supplier<Quotation> readFromA = Quotation::readFromA;
        Supplier<Quotation> readFromB = Quotation::readFromB;
        Supplier<Quotation> readFromC = Quotation::readFromC;

        var jobs = List.of(readFromA, readFromB, readFromC);

        var results = new ArrayList<Quotation>();

        jobs
                .stream()
                .parallel() // without parallel stream, everything will be executed in one thread
                .map(CompletableFuture::supplyAsync)
                .map(cf -> cf.thenApply(results::add)) // !!! this is a race condition if the stream is parallel
                .forEach(CompletableFuture::join); // wait for every CF to complete

        IO.println("Results: " + results);

        var end = Instant.now();
        IO.println("Duration from supplying to getting result: " + Duration.between(start, end).toMillis() + " ms");
    }
}
