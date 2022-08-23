package main;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {
    private Main mainObject = new Main();

    @Test
    public void assertFive() {
        assertEquals(mainObject.basicTest(), 5);
    }
}
