package checks.junit;

public class TestResultObject {
  String name; // name of JUNIT test (if provided and relevant)
  String desc; // description of JUNIT test (if provided and relevant)
  float value; // mark value of JUNIT test
  boolean result; // result of test mark, pass or fail (true/false)
  int orderOfAppearance; // appearance order of the test

  String returnFormatted() {
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
        + " ; "
        + desc
        + " ; Value: "
        + value
        + "% ; Mark received: "
        + passOrFail);
  }
}
