package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SimpleTest {

    @Test
    @DisplayName("Simple multiplication should work")
    public void someTest() {
        assertEquals(9, 3*3);
    }
}
