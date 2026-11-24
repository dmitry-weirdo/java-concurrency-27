package asýnc;

import java.util.Random;

public class DBService {

    private static final Random random = new Random(271L);

    public static String log(String message) {
        IO.println("Saving message \"" + message + "\" to the database...");

        try {
            Thread.sleep(random.nextInt(80, 120));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        IO.println("DB message \"" + message + "\" saved.");

        return "DB message \"" + message + "\" saved.";
    }
}
