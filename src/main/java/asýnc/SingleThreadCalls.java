package asýnc;

import java.time.Duration;
import java.time.Instant;

public class SingleThreadCalls {

    static void main() {
        var start = Instant.now();

        var quotationA = Quotation.readFromA();
        var quotationB = Quotation.readFromB();
        var quotationC = Quotation.readFromC();

        var end = Instant.now();

        IO.println(quotationA);
        IO.println(quotationB);
        IO.println(quotationC);

        IO.println("Duration: " + Duration.between(start, end).toMillis() + " ms");
    }
}
