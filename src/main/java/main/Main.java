package main;

import checks.runtime.RuntimeChecker;

public class Main {

  public int basicTest() {
    return 5;
  }

  public static void main(String[] args) {
    System.out.println("Hello World!");
    new Main();
  }

  public Main() {
    //TODO currently hardcoding the location of the exe.
//    runtimeCheck("/Volumes/projects/Comp4050_Team2_PizzaCrew/test_artifacts/sample_test/macos-x86_64/Flocking.app/Contents/MacOS/Flocking");
  }

  private void runtimeCheck(String executableLocation) {

    System.out.println("Runtime Check - Checking that the program does not crash.");

    RuntimeChecker runtimeCheck = new RuntimeChecker(executableLocation);
    if(!runtimeCheck.doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      return;
    }

    boolean result = runtimeCheck.runExecutable();

    System.out.println("Results: ");
    System.out.println("\tSuccess: " + result);
  }
}
