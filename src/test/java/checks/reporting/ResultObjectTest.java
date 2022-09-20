package checks.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class ResultObjectTest {
  String name; // name of JUNIT test (if provided and relevant)
  String desc; // description of JUNIT test (if provided and relevant)
  float value; // mark value of JUNIT test
  float result; // result of test mark, out of 100
  int orderOfAppearance; // appearance order of the test

  String returnFormatted() {
    // find a *neat* way of checking all variables have been instantiated before running this
    // method.
    return ("Test: "
        + orderOfAppearance
        + " ; "
        + desc
        + " ; Value: "
        + value
        + "% ; Mark received: "
        + result
        + " ; Contribution to total mark: "
        + result * (value / 100));
  }

  @Test
  public void returnFormattedTest() {
    name = "Object 1 output test";
    desc = "Description of test";
    value = 30;
    result = 63;
    orderOfAppearance = 1;
    assertEquals(
        "Test: 1 ; Description of test ; Value: 30.0% ; Mark received: 63.0 ; Contribution to total"
            + " mark: 18.900002",
        returnFormatted());
  }
}
