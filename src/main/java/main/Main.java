package main;

import checks.junit.JUnitRunner;
import org.apache.commons.lang3.RandomStringUtils;
import reporting.TestResult;
import checks.runtime.RuntimeChecker;
import checks.static_analysis.StaticAnalysisChecker;
import reporting.ReportMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;

// TODO test coverage.
// TODO comments.

// TODO Error handling.
// TODO clean up console output and CSV output

// Get rid of any TODOs
// Update README
// Create release branch with docs and instructions.

// Prerequisite - The students will submit a folder named their student id with the project folder inside IE 44613911/Flocking/flocking.pde

// TODO Make results folder.

public class Main {

  public static void main(String[] args) {
    new Main(args);
  }

  private void setup(String args[], Config config) {
    Utils.parseCommandLineArguments(args, config);
    config.checkOS();
    config.getWorkingDirectory();
  }

  private void runTests(Config config) {
    if(config.isRunIndividual())
    {
      if(config.isRunRuntimeCheck()) {
        runtimeCheck(config);
      }
      if(config.isRunStaticAnalysis()){
        staticAnalysisCheck(config);
      }
    }
    else {
      runtimeCheck(config);
      staticAnalysisCheck(config);
    }

    if (config.getJunitLocation() != null) {
      runJUNITTests(config);
    }
    System.out.println("Generating a CSV containing the results.");
    ReportMaker.addDataToCSV(config.getResultsCSVLocation());
  }

  public Main(String[] args) {
    Config config = new Config();
    setup(args, config);

    if (config.isRunMultiple()) {
      File projectsDir = new File(config.getProjectDirectory());
      for (File file : projectsDir.listFiles()) {
        if (!file.isDirectory()) continue;

        config.setProjectDirectory(file.getAbsolutePath());
        config.setTempLocation(
            config.getTempLocation() + "/" + RandomStringUtils.randomAlphanumeric(8));

        Utils.exportProcessingCodeToJava(config);
        config.setResultsCSVLocation("./" + file.toPath().getFileName() + ".csv");
        runTests(config);
      }
    } else {
      Utils.exportProcessingCodeToJava(config);
      runTests(config);
    }

    // Clean up the temporary folder.
    config.removeTemporaryFolder();
  }

  private void runJUNITTests(Config config) {
    JUnitRunner runner = new JUnitRunner();
    runner.runJUNITTests(config);
  }

  private void staticAnalysisCheck(Config config) {
    System.out.println("Static Analysis - Running static analysis tooling over the code.");

    StaticAnalysisChecker staticAnalysis = new StaticAnalysisChecker();

    // TODO create own ruleset - https://pmd.github.io/latest/pmd_rules_java.html
    ArrayList<String> arguments =
        new ArrayList<String>(
            Arrays.asList(
                "-d",
                config.getTempLocation(),
                "-R",
                "rulesets/java/quickstart.xml",
                "-f",
                "text"));

    if (config.isMac()) {
      arguments.add(0, "pmd");
    }

    String result = staticAnalysis.runExecutableWithArguments(config, arguments);
    TestResult testResult = new TestResult();
    testResult.name = "Static Analysis";
    testResult.desc = "The Static Analysis tool PMD has been run on the Java code to devise code quality.";
    testResult.testOutput = result;
    ReportMaker.addDataToReport(testResult);
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
