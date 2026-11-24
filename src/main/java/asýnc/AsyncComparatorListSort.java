package asýnc;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

public class AsyncComparatorListSort {

    static void main() {
        var counter = new Object() { // anonymous inner class that extends Object
            private int count = 0;
        };

        // comparator will be called asynchronously, but NOT concurrently
        Comparator<String> myComparator = (s1, s2) -> {
            counter.count++; // count how many times the comparator is called

            return s1.compareTo(s2);
        };

        // take a list of random ints
        var list = new Random(666L)
                .ints(1_000)
                .mapToObj(Objects::toString)
                .toList();

        IO.println("Number of unsorted strings: " + list.size());
        IO.println("Unsorted strings: " + list);

        var sortedList = list
                .stream()
                .sorted(myComparator)
                .toList();

        IO.println();
        IO.println("Number of sorted strings: " + sortedList.size());
        IO.println("Sorted strings: " + sortedList);

        IO.println("Total calls to comparator: " + counter.count);
    }
}
