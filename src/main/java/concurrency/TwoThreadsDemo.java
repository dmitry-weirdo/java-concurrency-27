package concurrency;

public class TwoThreadsDemo {

    private static StringBuffer sb = new StringBuffer(); // StringBuffer is thread-safe

    private static void log(String s) {
        sb.append(s).append("\n");
    }

    private static class ThreadExample implements Runnable {
        private String name;

        public ThreadExample(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.printf("%s: %s %n", name, i);
                log(String.format("%s: %s", name, i));
            }
        }
    }

    static void main() {
        var t1 =  new Thread(new ThreadExample("t1"));
        var t2 =  new Thread(new ThreadExample("t2"));

        // setting priority still does NOT guarantee that t1 will execute before t2
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);

        System.out.println("Starting threads");
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End of main");
        System.out.println("Result: \n" + sb);
        System.out.println("End of result!");
    }
}


