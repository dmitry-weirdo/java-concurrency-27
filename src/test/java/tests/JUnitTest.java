package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JUnitTest {

    @Test
    void testJUnit() {
        Assertions.assertEquals(333, 100 + 200 + 33); // expected comes first in JUnit
    }
}
