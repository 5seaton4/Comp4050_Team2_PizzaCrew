package main;

import checks.junit.JUnitHelper;
import reporting.TestResult;
import checks.runtime.RuntimeChecker;
import checks.static_analysis.StaticAnalysisChecker;
import reporting.ReportMaker;

import java.util.ArrayList;
import java.util.Arrays;

//TODO change all function and variable names to be consistent.
//TODO Update README
//TODO working on Windows.

//TODO processing-java must be installed

//TODO test coverage.
//TODO comments.

public class Main {

  public static void main(String[] args) {
    new Main(args);
  }

  private void setup(String args[], Config config)
  {
    Utils.parseCommandLineArguments(args, config);
    config.checkOS();
    Utils.exportProcessingCodeToJava(config);
  }

  public Main(String[] args) {
    Config config = new Config();
    setup(args, config);

    runtimeCheck(config);
    staticAnalysisCheck(config);
    runJUNITTests();

    System.out.println("Generating a CSV containing the results.");
    ReportMaker.addDataToCSV(config.RESULT_CSV_LOCATION);

    //Clean up the temporary folder.
    config.removeTemporaryFolder();
  }

  //TODO we need to pass in JUNIT tests and have them run on the exported processing code.
  private void runJUNITTests()
  {
    TestResult teo1 = new TestResult();
    TestResult teo2 = new TestResult();

    JUnitHelper.runTests(teo1, teo2);
    ReportMaker.addDataToReport(teo1);
    ReportMaker.addDataToReport(teo2);
  }

  private void staticAnalysisCheck(Config config)
  {
    System.out.println("Static Analysis - Running static analysis tooling over the code.");

    StaticAnalysisChecker staticAnalysis = new StaticAnalysisChecker();

    //TODO these arguments need configuring.
    ArrayList<String> arguments = new ArrayList<String>(
    Arrays.asList( "pmd",
            "-d",
            config.getTempLocation(),
            "-R",
            "rulesets/java/quickstart.xml",
            "-f",
            "text"));

    //TODO this needs to capture the output to determine if its passed or failed, and then add it to the results.
    staticAnalysis.runExecutableWithArguments(config, arguments);
  }

  private void runtimeCheck(Config config) {

    System.out.println("Runtime Check - Checking that the program does not crash.");
    RuntimeChecker runtimeCheck = new RuntimeChecker();
    boolean result = runtimeCheck.runExecutable(config);

    System.out.println("Results: ");
    System.out.println("\tSuccess: " + result);

    TestResult testResult = new TestResult();
    testResult.name = "Runtime Check";
    testResult.desc = "The program is run to ascertain whether there is any runtime errors.";
    testResult.result = true;
    ReportMaker.addDataToReport(testResult);
  }
}
