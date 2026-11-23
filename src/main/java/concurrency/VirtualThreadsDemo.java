package concurrency;

public class VirtualThreadsDemo {

    static void main() {
        var runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Virtual Threads Demo");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Virtual Thread end after sleeping.");
            }
        };

        System.out.println("Starting a virtual thread.");

        Thread virtualThread = Thread.startVirtualThread(runnable); // will still be a thread!
        System.out.println("Virtual thread is daemon: " + virtualThread.isDaemon());
//        virtualThread.setDaemon(false); // IAE
//        virtualThread.setDaemon(true); // IllegalThreadStateException

        // for waiting for the daemon thread, we can join on it
        try {
            virtualThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("End of main");
    }
}
