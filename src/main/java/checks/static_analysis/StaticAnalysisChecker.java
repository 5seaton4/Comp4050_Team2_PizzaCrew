package checks.static_analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StaticAnalysisChecker {

  private String pathToExecutable;

  public StaticAnalysisChecker(String pathToExecutable) {
    this.pathToExecutable = pathToExecutable.trim();
  }

  public boolean doesExecutableExist() {
    File executable = new File(pathToExecutable);
    if (executable.isFile()) {
      return true;
    } else {
      return false;
    }
  }

  public void runExecutableWithArguments(String[] arguments) {

    System.out.println("Running static analysis tool at " + pathToExecutable);

    try {
      Runtime runtime = Runtime.getRuntime();
      // Run the executable.
      Process process = runtime.exec(arguments);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(new 
            InputStreamReader(process.getErrorStream()));

        // Read the output from the command
        System.out.println("Static Analysis Output\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

    // Read any errors from the attempted command
    System.out.println("Process errors of the command (if any):\n");
    while ((s = stdError.readLine()) != null) {
        System.out.println(s);
    }

      System.out.println("Closing executable");
      process.destroy();
    } catch (IOException e) {
      // TODO(Jack): Error handling
      e.printStackTrace();
    }
  }
}
