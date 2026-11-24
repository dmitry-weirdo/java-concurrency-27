package concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelStreamsDemo2 {

    private static class MutableInteger {
        private int value = 0;

        public synchronized void increment() {
            value++; // !!! non-atomic operation -> need to synchronize
        }

        public int getValue() {
            return value;
        }
    }

    static void main() {
        MutableInteger mutableInteger = new MutableInteger();

        int incrementsCount = 1_000_000;

        IntStream.range(0, incrementsCount)
                .unordered()
                .parallel() // will be NOT 1_000_000, execute on multiple threads
                .forEach(_ -> mutableInteger.increment());

        System.out.printf("MutableInteger value after %s increments: %s. %n", incrementsCount, mutableInteger.getValue());

    }
}
