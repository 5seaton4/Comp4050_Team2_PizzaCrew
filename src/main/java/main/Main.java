package main;

import checks.runtime.RuntimeChecker;
import checks.static_analysis.StaticAnalysisChecker;

public class Main {

  public int basicTest() {
    return 5;
  }

  public static void main(String[] args) {
    System.out.println("Hello World!");
    new Main();
  }

  public Main() {
    // TODO currently hardcoding the location of the exe.
    StaticAnalysisCheck();
    // runtimeCheck("/Volumes/projects/Comp4050_Team2_PizzaCrew/test_artifacts/sample_test/macos-x86_64/Flocking.app/Contents/MacOS/Flocking");
  }

  private void StaticAnalysisCheck() {
    String executableLocation = "/Volumes/projects/Comp4050_Team2_PizzaCrew/tools/pmd-bin-6.49.0/bin/run.sh";
    String arguments[] = {"/Volumes/projects/Comp4050_Team2_PizzaCrew/tools/pmd-bin-6.49.0/bin/run.sh", "pmd", "-d", "/Volumes/projects/Comp4050_Team2_PizzaCrew/temp", "-R", "rulesets/java/quickstart.xml", "-f", "text"};

    StaticAnalysisChecker staticAnalysis = new StaticAnalysisChecker(executableLocation);
    if (!staticAnalysis.doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      return;
    }

    staticAnalysis.runExecutableWithArguments(arguments);
  }

  private void runtimeCheck(String executableLocation) {

    System.out.println("Runtime Check - Checking that the program does not crash.");

    RuntimeChecker runtimeCheck = new RuntimeChecker(executableLocation);
    if (!runtimeCheck.doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      return;
    }

    boolean result = runtimeCheck.runExecutable();

    System.out.println("Results: ");
    System.out.println("\tSuccess: " + result);
  }
}
