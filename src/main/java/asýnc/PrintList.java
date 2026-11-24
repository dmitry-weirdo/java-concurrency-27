package asýnc;

import java.util.List;
import java.util.function.Consumer;

public class PrintList {

    static void main() {
//        List<String> list = List.of("one", "two", "three");
        List<String> list = List.of(); // empty list will be 0 calls

        Consumer<String> println = obj -> {
            IO.println("Printing:");
            IO.println(obj);
        };

        list.forEach(println);
    }
}
