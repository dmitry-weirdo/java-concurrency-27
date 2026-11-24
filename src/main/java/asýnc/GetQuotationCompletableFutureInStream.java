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

/*
        var results = new ArrayList<Quotation>();

        jobs
                .stream()
                .parallel() // without parallel stream, everything will be executed in one thread
                .map(CompletableFuture::supplyAsync)
                .map(cf -> cf.thenApply(results::add)) // !!! this is a race condition if the stream is parallel
                .forEach(CompletableFuture::join); // wait for every CF to complete
*/

        var tasks = jobs
                .stream() // !!! NO parallel
                .map(CompletableFuture::supplyAsync)
                .toList();

        var doneSupplying = Instant.now();
        IO.println("Duration of supplying: " + Duration.between(start, doneSupplying).toMillis() + " ms");

        // wait for every CF to complete
//        tasks.forEach(CompletableFuture::join);

        List<Quotation> results = tasks
                .stream()
                .map(CompletableFuture::join)
                .toList();

        IO.println("Results: " + results);

        var end = Instant.now();
        IO.println("Duration from supplying to getting the result: " + Duration.between(doneSupplying, end).toMillis() + " ms");
    }
}
