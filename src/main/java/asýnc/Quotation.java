package asýnc;

import java.util.Random;

public record Quotation(String agency, int price) {

    private static final Random random = new Random(314L);

    public static boolean debug = false;

    static Quotation readFromA() {
        return readFromAgency("A");
    }

    static Quotation readFromB() {
        return readFromAgency("B");
    }

    static Quotation readFromC() {
        return readFromAgency("C");
    }

    private static Quotation readFromAgency(String agencyName) {
        try {
            if (debug) {
                System.out.printf("Reading quotation from Agency %s in thread %s %n", agencyName, Thread.currentThread());
            }

            Thread.sleep(random.nextInt(80, 120));

            return new Quotation(
                    "Agency " + agencyName,
                    random.nextInt(80, 120)
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
