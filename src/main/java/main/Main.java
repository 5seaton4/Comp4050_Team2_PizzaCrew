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

/**
 * This class is the main class of the application, all main functions are called from this class.
 *
 * @author Pizza Crew
 * @version 1.0
 * @since 2022-10-25
 */
public class Main {

  public static void main(String[] args) {
    new Main(args);
  }

  /**
   * This function will setup our application to be able to run the tests. It is responsible for
   * parsing the cmd line output, checking the users OS, calculating the working directory and
   * creating a directory to hold the results.
   *
   * @param args array of arguments passed into the program.
   * @param config the config class which will hold all needed config for the application.
   */
  private void setup(String args[], Config config) {
    // Parse the command line arguments
    Utils.parseCommandLineArguments(args, config);
    // Check the user is on a compatible OS
    config.checkOS();
    // Calculate the working directory which we use for creating temporary folders
    config.getWorkingDirectory();
    // Create a results directory to store all the CSVs
    config.createResultsDirectory();
  }

  /**
   * This function will call all the functions which are responsible for running the tests. It will
   * either run individual tests based on the users input or will run all tests. It will then call
   * the function which will output the CSV containing the results.
   *
   * @param config the config class which holds all needed config for the application.
   */
  private void runTests(Config config) {
    // If the user has specified certain tests run those otherwise run all tests.
    if (config.isRunIndividual()) {
      if (config.isRunRuntimeCheck()) {
        System.out.println("Runtime Check - Checking that the program does not crash.");
        runtimeCheck(config);
      }
      if (config.isRunStaticAnalysis()) {
        System.out.println("Static Analysis - Running static analysis tooling over the code.");
        staticAnalysisCheck(config);
      }
    } else {
      System.out.println("Runtime Check - Checking that the program does not crash");
      runtimeCheck(config);
      System.out.println("Static Analysis - Running static analysis tooling over the code.");
      staticAnalysisCheck(config);
    }

    if (config.getJunitLocation() != null) {
      System.out.println("JUNIT Tests - Running user defined JUNIT tests");
      runJUNITTests(config);
    }

    System.out.println("Generating Results At " + config.getResultsCSVLocation());
    ReportMaker.addDataToCSV(config.getResultsCSVLocation());
    ReportMaker.reset();
    System.out.println("Finished");
  }

  /**
   * The main class constructor which if the program is running multiple projects, will loop over
   * the projects and call the functions to run them individually. If not, it will call run tests on
   * the one project passed in by the user.
   *
   * @param args the args the user passed into the program.
   */
  public Main(String[] args) {
    Config config = new Config();
    setup(args, config);

    // If the user has passed in multiple projects to be tested then loop over the projects and run
    // them individually.
    if (config.isRunMultiple()) {
      File projectsDir = new File(config.getProjectDirectory());
      String oldTempLocation = config.getTempLocation();
      for (File file : projectsDir.listFiles()) {
        if (!file.isDirectory()) continue;

        config.setProjectDirectory(file.getAbsolutePath());
        System.out.println("Running tests on project: " + config.getProjectDirectory());
        config.setTempLocation(
            config.getTempLocation() + "/" + RandomStringUtils.randomAlphanumeric(8));

        System.out.println("Exporting processing code to java");
        Utils.exportProcessingCodeToJava(config);
        config.setResultsCSVLocation("./" + "Results/" + file.toPath().getFileName() + ".csv");
        runTests(config);
      }
      //Remove entire temp folder
      config.setTempLocation(oldTempLocation);
    } else {
      System.out.println("Running tests on project: " + config.getProjectDirectory());
      // If the user has only passed in one project.
      System.out.println("Exporting processing code to java");
      Utils.exportProcessingCodeToJava(config);
      config.setResultsCSVLocation("./" + "Results/" + "results.csv");
      runTests(config);
    }

    // Clean up the temporary folder.
    config.removeTemporaryFolder();
  }

  /**
   * This function will run the JUNIT tests module.
   *
   * @param config the config class which holds all needed config for the application.
   */
  private void runJUNITTests(Config config) {
    JUnitRunner runner = new JUnitRunner();
    runner.runJUNITTests(config);
  }

  /**
   * This function will run the static analysis tests module.
   *
   * @param config the config class which holds all needed config for the application.
   */
  private void staticAnalysisCheck(Config config) {

    StaticAnalysisChecker staticAnalysis = new StaticAnalysisChecker();

    // The arguments that are passed to the PMD executable
    ArrayList<String> arguments =
        new ArrayList<String>(
            Arrays.asList(
                "-d",
                config.getTempLocation(),
                "-R",
                "rulesets/java/quickstart.xml",
                "-f",
                "text"));

    // If the user is running on OS we want to add another argument.
    if (config.isMac()) {
      arguments.add(0, "pmd");
    }

    String result = staticAnalysis.runExecutableWithArguments(config, arguments);
    if (result == "") {
      System.err.println("Failed to run Static Analysis module.");
      return;
    }

    // Add the results of the static analysis to the report.
    TestResult testResult = new TestResult();
    testResult.name = "Static Analysis";
    testResult.desc =
        "The Static Analysis tool PMD has been run on the Java code to devise code quality.";
    testResult.testOutput = result;
    ReportMaker.addDataToReport(testResult);
  }

  /**
   * This function will run the runtime check module.
   *
   * @param config the config class which holds all needed config for the application.
   */
  private void runtimeCheck(Config config) {
    RuntimeChecker runtimeCheck = new RuntimeChecker();
    boolean result = runtimeCheck.runExecutable(config);

    TestResult testResult = new TestResult();
    testResult.name = "Runtime Check";
    testResult.desc = "The program is run to ascertain whether there is any runtime errors.";
    testResult.result = true;
    testResult.value = "N/A";
    testResult.testOutput = "N/A";
    testResult.numberResult = true;
    ReportMaker.addDataToReport(testResult);
  }
}
