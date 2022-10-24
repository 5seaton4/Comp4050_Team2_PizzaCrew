package checks.static_analysis;

import main.Config;
import reporting.TestResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVWriter;

import reporting.TestResult;

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

  public String runExecutableWithArguments(Config config, ArrayList<String> arguments) {
    pathToExecutable = config.getStaticAnalysisLocation();

    if (!doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      return "";
    }

    System.out.println("Running static analysis tool at " + pathToExecutable);

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

      String csvFilename = "C:/Users/abhin/Documents/COMP4050Pizza/Comp4050_Team2_PizzaCrew/StaticResults.csv";
      try {
          CSVWriter writer = new CSVWriter(new FileWriter(csvFilename));
          List<String[]> csvData = new ArrayList<String[]>();
          
          String[] header="Line Number,Error,Description".split(",");
          csvData.add(header);

          for(int z=0;z<list.size();z++) {
            String str="";
            System.out.println("OUPUTTTTTTTTTT" + list.get(z).size());
              for(int y=2;y<list.get(z).size();y++) {
                 str += list.get(z).get(y);
            }
            String[] content = str.split(",");
            csvData.add(content);
          }
          
          writer.writeAll(csvData);
          writer.close();
          System.out.println("CSV file created succesfully.");
      } catch (Exception e) {
          System.out.println("exception :" + e.getMessage());
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

    return result;
  }
}
