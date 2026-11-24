package asýnc;

import java.util.Random;

public class EmailService {

    private static final Random random = new Random(271L);

    public static String sendEmail(Quotation quotation) {
        IO.println("Sending email with " + quotation + "...");

        try {
            Thread.sleep(random.nextInt(80, 120));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        IO.println("Email with " + quotation + " sent.");

        return "Email sent for quotation " + quotation;
    }
}
