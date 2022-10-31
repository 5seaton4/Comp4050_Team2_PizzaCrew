package reporting;

/**
 * This class is responsible for holding the test results of the test module which then gets passed
 * to the ReportMaker class to be turned into a CSV.
 */
public class TestResult {
  public String name; // name of test (if provided and relevant)
  public String desc; // description of test (if provided and relevant)
  public String value; // mark value of test
  public boolean result; // result of test mark, pass or fail (true/false)
  public boolean numberResult;
  public String testOutput;

  public String[] returnFormatted() {
    String passOrFail = "Fail";

    if (result) {
      passOrFail = "Pass";
    }

    String[] row = {
            name,
            desc,
            (numberResult ? value : "N/A"),
            (numberResult ? passOrFail : "N/A"),
            (testOutput != "" ? testOutput : "N/A")
    };

    return row;
  }
}
