package concurrency;

public class Main {
    static void main() {
        System.out.println("This is my main class on Java 27 project.");

        Runnable job = new ThreadExample("My thread");

        Thread thread = new Thread(job);
        thread.start(); // start a new thread

        // use thread.join to wait for the 2nd thread
        try {
            thread.join(); // join without argument will wait forever
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End of main");
    }

    private static class ThreadExample implements Runnable {
        private final String name;

        public ThreadExample(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            final var SECONDS_TO_SLEEP = 5;

            try {
                System.out.printf("%s: Sleeping for %s seconds... %n", name, SECONDS_TO_SLEEP);

                Thread.sleep(SECONDS_TO_SLEEP * 1000); // sleep is a static method and works for the current thread from where it is called

                System.out.printf("%s: Thread woke up! %n", name);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < 10; i++) {
                System.out.printf("%s: %s %n", name, i);
            }
        }
    }
}
