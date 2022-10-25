package checks.static_analysis;

import main.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class holds all the Static Analysis module code. It includes helper functions and the function which runs the static analysis tool.
 */

public class StaticAnalysisChecker {

  //Holds the path to the static analysis tool.
  private String pathToExecutable;

  public StaticAnalysisChecker() {}

  /**
   * This function will check if the static analysis executable can be found.
   * @return
   */
  public boolean doesExecutableExist() {
    File executable = new File(pathToExecutable);
    if (executable.isFile()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * This function is responsible for running the static analysis tool with arguments passed into the function.
   *
   * @param config the config class which holds all needed config for the application.
   * @param arguments the arguments that will be passed to the PMD application.
   * @return
   */

  public String runExecutableWithArguments(Config config, ArrayList<String> arguments) {
    pathToExecutable = config.getStaticAnalysisLocation();

    if (!doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      return "";
    }

    arguments.add(0, pathToExecutable);

    String result = "";

    try {
      Runtime runtime = Runtime.getRuntime();

      // Run the executable.
      Process process =
          runtime.exec(Arrays.copyOf(arguments.toArray(), arguments.size(), String[].class));

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      // Read the output from the command
      System.out.println("Static Analysis Display\n");
      String s = null;
      while ((s = stdInput.readLine()) != null) {
        result += s;
      }

      // Read any errors from the attempted command
      System.out.println("Process errors of the command (if any):\n");
      while ((s = stdError.readLine()) != null) {
        System.out.println(s);
      }

      System.out.println("Closing executable");
      process.destroy();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }
}
