package main;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;

public class ConfigTest {
  @Test
  public void testCreatingResultFolder() {
    Config config = new Config();
    File file = new File("./Results");
    file.mkdir();
    // This should not crash.
    try {
      config.createResultsDirectory();
      assertEquals(true, true);
    } catch (Exception e) {
      assertEquals(true, false);
    }
  }
}
