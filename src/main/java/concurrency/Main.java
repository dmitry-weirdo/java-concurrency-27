package concurrency;

public class Main {
    static void main() {
        System.out.println("This is my main class on Java 27 project.");

        Runnable job = new ThreadExample("My thread");

        Thread thread = new Thread(job);
        thread.start(); // start a new thread

        System.out.println("End of main");
    }

    private static class ThreadExample implements Runnable {
        private final String name;

        public ThreadExample(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.printf("%s: %s %n", name, i);
            }
        }
    }
}
