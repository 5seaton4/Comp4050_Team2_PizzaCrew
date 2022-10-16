package reporting;

public class TestResult {
  // TODO these variables need to be accessed via getters and setters.
  public String name; // name of test (if provided and relevant)
  public String desc; // description of test (if provided and relevant)
  public float value; // mark value of test
  public boolean result; // result of test mark, pass or fail (true/false)
  public int orderOfAppearance; // appearance order of the test

  public String returnFormatted() {
    // find a *neat* way of checking all variables have been instantiated before running this
    // method.
    String passOrFail;
    if (result == true) {
      passOrFail = "Pass";
    } else {
      passOrFail = "Fail";
    }
    return ("Test: "
        + orderOfAppearance
        + " , "
        + desc
        + " , Value: "
        + value
        + "% , Mark received: "
        + passOrFail);
  }
}
