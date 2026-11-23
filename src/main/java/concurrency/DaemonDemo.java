package concurrency;

public class DaemonDemo {
    static void main() {
        new WorkerThread().start();

        System.out.println("Main thread is sleeping...");

        try {
            Thread.sleep(7500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main Thread ending.");
    }
}

class WorkerThread extends Thread {

    public WorkerThread() {
        this.setDaemon(true);
    }

    @Override
    public void run() {
        int count = 0;

        // for non-daemon thread, this will work infinitely
        // for daemon thread, this will terminate together with the main thread
        while (true) {
            System.out.println("Hello from Worker: " + count++);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
