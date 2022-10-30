import static org.junit.Assert.*;
import org.junit.Test;

public class TestFile {
  @Test
  public void test() {
    Flocking flocking = new Flocking();
    assertEquals(true, true);
  }

  @Test
  public void testFail() {
    assertEquals(true, false);
  }

  @Test
  public void intTest() {
    assertEquals(1, 1);
  }

  public void testFail2() {
    assertEquals(1, 2);
  }
}
