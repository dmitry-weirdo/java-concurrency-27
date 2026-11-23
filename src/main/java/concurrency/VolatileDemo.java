package concurrency;

public class VolatileDemo {

    // If we do NOT set volatile, each thread will have its own copy of the #running variable
    private static volatile boolean running = true;

    private static final StringBuffer sb = new StringBuffer(); // StringBuffer is thread-safe

    private static void log(String s) {
        sb.append(s + "\n");
//        sb.append(s).append("\n"); // this can cause splitting two .append() calls between 2 threads
    }

    static void main() {
        var t1 = new Thread(() -> {
            int counter = 0;

            while (running) {
                counter++;
            }

            log("Thread t1 finished. Counted up to " + counter);
        });

        var t2 = new Thread(() -> {
            try {
                System.out.println("Thread t2 is sleeping.");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            log("Thread t2 is finishing. Setting running to false.");

            running = false;
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End of main.");
        System.out.println("Result: " + sb);
    }
}
