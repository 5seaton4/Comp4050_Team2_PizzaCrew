package main;

import checks.junit.JUnitRunner;
import reporting.TestResult;
import checks.runtime.RuntimeChecker;
import checks.static_analysis.StaticAnalysisChecker;
import reporting.ReportMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File; 
import java.util.List; 
import java.util.function.ToLongBiFunction;

// TODO test coverage.
// TODO comments.

public class Main {

  public static void main(String[] args) {
    new Main(args);
  }

  private void setup(String args[], Config config) {
    Utils.parseCommandLineArguments(args, config);
    config.checkOS();
    config.getWorkingDirectory();
    Utils.exportProcessingCodeToJava(config);
  }

  public Main(String[] args) {
    Config config = new Config();
    setup(args, config);

    if (config.isRunIndividual()) {
      if (config.isRunRuntimeCheck()) {
        runtimeCheck(config);
      }
      if (config.isRunStaticAnalysis()) {
        staticAnalysisCheck(config);
      }
    } else {
      runtimeCheck(config);
      staticAnalysisCheck(config);
    }
    if (config.getJunitLocation() != null) {
      runJUNITTests(config);
    }

    System.out.println("Generating a CSV containing the results.");
    ReportMaker.addDataToCSV(config.RESULT_CSV_LOCATION);

    // Clean up the temporary folder.
    config.removeTemporaryFolder();

    
    //Multithreading for running multiple tests at once
    File filePath = new File("C:\\Users\\Priyanshi Patel\\Documents\\MixtureGrid");
    File filesList[] = filePath.listFiles();

    int numberofThreads = 2;
    Thread[] threads = new Thread[numberofThreads];

    final int filesPerThread = filesList.length / numberofThreads;
    final int remainingFiles = filesList.length % numberofThreads;

    for (int t = 0; t < numberofThreads; t++) {
      final int thread = t;
      threads[t] = new Thread() {
        @Override
        public void run() {
          runThread(filesList, numberofThreads, thread, filesPerThread, remainingFiles);
        }
      };
    }
    for (Thread t1 : threads)
      t1.start();
    for (Thread t2 : threads)
      try {
        t2.join();
      } catch (InterruptedException e) {
      }

  }

  private static void runThread(File[] filesList, int numberofThreads, int thread, int filesPerThread, int remainingFiles) {
    //assigning files equallyto each thread and assigning remaining files to last thread
    List<File> inFiles = new ArrayList<File>();
    for(int i = thread * filesPerThread; i < (thread + 1) * filesPerThread; i++) {
      inFiles.add(filesList[i]);
    }
    if(thread == numberofThreads - 1 && remainingFiles > 0) {
      for(int j = filesList.length - remainingFiles; j < filesList.length; j++) {
        inFiles.add(filesList[j]);
      
        //process files
        for(File file : inFiles) {
          System.out.println("Processing file: " + file.getName() + " on thread: " + Thread.currentThread().getName());
          }
        }
      }
    }


  private void runJUNITTests(Config config) {
    JUnitRunner runner = new JUnitRunner();
    runner.runJUNITTests(config);
  }

  private void staticAnalysisCheck(Config config) {
    System.out.println("Static Analysis - Running static analysis tooling over the code.");

    StaticAnalysisChecker staticAnalysis = new StaticAnalysisChecker();

    // TODO these arguments need configuring.
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

    // TODO this needs to capture the output to determine if its passed or failed, and then add it
    // to the results.
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
