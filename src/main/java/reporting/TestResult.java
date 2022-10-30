package reporting;

/**
 * This class is responsible for holding the test results of the test module which then gets passed
 * to the ReportMaker class to be turned into a CSV.
 */
public class TestResult {
  public String name; // name of test (if provided and relevant)
  public String desc; // description of test (if provided and relevant)
  public float value = -1; // mark value of test
  public boolean result; // result of test mark, pass or fail (true/false)
  public boolean numberResult;
  public int orderOfAppearance; // appearance order of the test
  public String testOutput;

  public String returnFormatted() {
    // find a *neat* way of checking all variables have been instantiated before running this
    // method.
    String passOrFail;
    if (result == true) {
      passOrFail = "Pass";
    } else {
      passOrFail = "Fail";
    }

    String csvRow =
        "Test: "
            + orderOfAppearance
            + " ; "
            + desc
            + " ; Value: "
            + (numberResult ? value : "N/A")
            + "% ; Mark Received: "
            + (numberResult ? passOrFail : "N/A")
            + "; Test Output: "
            + (testOutput != "" ? testOutput : "N/A");
    return csvRow;
  }
}
