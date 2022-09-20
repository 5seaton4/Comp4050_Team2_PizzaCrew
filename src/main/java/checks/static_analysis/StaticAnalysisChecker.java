package checks.static_analysis;

import main.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticAnalysisChecker {

  private String pathToExecutable;

  public StaticAnalysisChecker()
  {
  }

  public boolean doesExecutableExist() {
    File executable = new File(pathToExecutable);
    if (executable.isFile()) {
      return true;
    } else {
      return false;
    }
  }

  public void runExecutableWithArguments(Config config, ArrayList<String> arguments)
  {
    pathToExecutable = config.getStaticAnalysisLocation();

    if (!doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      return;
    }
    System.out.println("Running static analysis tool at " + pathToExecutable);

    arguments.add(0, pathToExecutable);

    try {
      Runtime runtime = Runtime.getRuntime();
      // Run the executable.
      //TODO convoluted way of turning an ArrayList into an array..
      Process process = runtime.exec(Arrays.copyOf(arguments.toArray(), arguments.size(), String[].class));

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

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
