package checks.static_analysis;

import main.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class StaticAnalysisChecker {

  private String pathToExecutable;

  public StaticAnalysisChecker() {}

  public boolean doesExecutableExist() {
    File executable = new File(pathToExecutable);
    if (executable.isFile()) {
      return true;
    } else {
      return false;
    }
  }

  public void runExecutableWithArguments(Config config, ArrayList<String> arguments) {
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
      // TODO convoluted way of turning an ArrayList into an array..
      Process process =
          runtime.exec(Arrays.copyOf(arguments.toArray(), arguments.size(), String[].class));

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      // Read the output from the command
      System.out.println("Static Analysis Display\n");
      String s = null;
      ArrayList<ArrayList<String>> list =  new ArrayList<ArrayList<String>>(); 
      ArrayList<String> subList = new ArrayList<String>();
      int a = 0;
      while ((s = stdInput.readLine()) != null) {

        String[] arr = s.split(" ");

        System.out.println("Arrays to String");
        System.out.println(Arrays.toString(arr));

        System.out.println("Using a loop and size of array is: " + arr.length);
        for(int k=0;k<arr.length;k++) {
          System.out.println(arr[k]);
        }
        // for(int i=0;i<=1;i++){
        //   list.get(a).add(arr[i]);
        // }
        // a++;

        // for(int j=0;j<list.size();j++) {
        //   System.out.println(list.get(j).get(0));
        //   System.out.println(list.get(j).get(1));
        //   System.out.println(list.get(j).get(2));
        // }
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
