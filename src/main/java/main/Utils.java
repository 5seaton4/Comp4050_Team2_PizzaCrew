package main;

import org.apache.commons.cli.*;
import reporting.ReportMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is responsible for holding utility functions that are used else where in the program.
 */

public class Utils {

  /**
   * This function will parse the command line arguments passed in by the user and will set variables in the config class to be used elsewhere
   * in the program.
   * @param args the arguments passed in by the User.
   * @param config the config class which holds all needed config for the application.
   */

  public static void parseCommandLineArguments(String[] args, Config config) {
    Options options = new Options();

    Option processingLocation =
        new Option("p", "processing-java-location", true, "Processing exe location");
    processingLocation.setRequired(true);
    options.addOption(processingLocation);

    Option projectLocation =
        new Option("t", "project-location", true, "Processing project to be tested location");
    projectLocation.setRequired(true);
    options.addOption(projectLocation);

    Option testFile =
        new Option("j", "junit-location", true, "JUNIT file to run against the processing code");
    testFile.setRequired(false);
    options.addOption(testFile);

    Option runStaticOption =
        new Option("a", "run-static-analysis", false, "Run the static analysis test");
    runStaticOption.setRequired(false);
    options.addOption(runStaticOption);

    Option runRuntimeOption =
        new Option("r", "run-runtime-check", false, "Run the runtime check test");
    runRuntimeOption.setRequired(false);
    options.addOption(runRuntimeOption);

    Option multipleProjectsOption =
        new Option("m", "multiple-projects", false, "Passing in Multiple Projects");
    multipleProjectsOption.setRequired(false);
    options.addOption(multipleProjectsOption);

    Option studentIdOption = new Option("s", "student-id", true, "Student ID");
    studentIdOption.setRequired(false);
    options.addOption(studentIdOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

    //Try to parse the command line, if there are errors then print the help message.
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("utility-name", options);
      System.exit(1);
    }

    String processingFileLocation = cmd.getOptionValue("processing-java-location");
    String projectDir = cmd.getOptionValue("project-location");
    String studentId = cmd.getOptionValue("student-id");

    if (cmd.hasOption("r") || cmd.hasOption("a")) {
      config.setRunIndividual(true);
    }

    if (cmd.hasOption("r")) {
      config.setRunRuntimeCheck(true);
    }

    if (cmd.hasOption("a")) {
      config.setRunStaticAnalysis(true);
    }

    if (cmd.hasOption("m")) {
      config.setRunMultiple(true);
    }

    // Check the locations are valid.
    File processingExe = new File(processingFileLocation);
    File projectFile = new File(projectDir);
    if (!processingExe.isFile()) {
      System.err.println(
          "Error processing-java-location passed in does not exist or is a directory.");
      System.exit(1);
    }
    if (!projectFile.isDirectory()) {
      System.err.println("Error project location passed in does not exist or is not a directory.");
      System.exit(1);
    }

    String junitFileLocation = cmd.getOptionValue("junit-location");
    // Check the locations are valid.
    if (junitFileLocation != null) {
      File junitFile = new File(junitFileLocation);
      if (!junitFile.isFile()) {
        System.err.println("Error junit-location passed in does not exist or is a directory.");
        System.exit(1);
      }
      config.setJunitLocation(junitFileLocation);
    }

    config.setProcessingLocation(processingFileLocation);
    config.setProjectDirectory(projectDir);
    if (studentId != null) {
      ReportMaker.STUDENT_ID = studentId;
    }
  }

  /**
   * This function will export the processing code to java. It will also create the temporary location that the files will go into.
   * @param config the config class which holds all needed config for the application.
   */

  public static void exportProcessingCodeToJava(Config config) {
    System.out.println("Exporting processing code to Java.");

    String platform = "windows";
    if (config.isMac()) {
      platform = "macosx";
    }

    // Create a temp directory for the java code.
    Path tempPath = Paths.get(config.getTempLocation());

    File tempFilePath = new File(config.getTempLocation());
    if(tempFilePath.isDirectory()) {
      config.removeTemporaryFolder();
    }

    File projectDir = new File(config.getProjectDirectory());
    File[] projectDirFiles = projectDir.listFiles();
    int dirCount = 0;
    int dirId = 0;
    for (int i = 0; i < projectDirFiles.length; i++) {
      File file = projectDirFiles[i];
      if (file.isDirectory()) {
        dirCount++;
        dirId = i;
      }
    }

    if (dirCount != 1) {
      System.err.println(
          "Error project directory should only contain one folder containing the PDE files.");
      System.exit(1);
    }

    String[] arguments = {
      config.getProcessingLocation(),
      "--sketch=" + projectDirFiles[dirId],
      "--output=" + tempPath.toString(),
      "--export",
      "--force",
      "--variant=" + platform
    };

    try {
      Runtime runtime = Runtime.getRuntime();
      // Run the executable.
      Process process = runtime.exec(arguments);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      // Read the output from the command
      System.out.println("Processing export output: \n");
      String s = null;
      while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
      }

      // Read any errors from the attempted command
      System.out.println("Process errors of the command (if any):\n");
      while ((s = stdError.readLine()) != null) {
        System.out.println(s);
      }

      System.out.println("Closing process.");
      process.destroy();

      //Calculates the app name from the processing folder name.
      Utils.getAppNameFromExecutable(config);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This function will get the app name from the processing folder name.
   * @param config the config class which holds all needed config for the application.
   */

  private static void getAppNameFromExecutable(Config config) {
    File directory = new File(config.getTempLocation());
    String contents[] = directory.list();
    boolean found = false;
    for (String folder : contents) {
      System.out.println(folder);
      if (folder.contains(".app") || folder.contains(".exe")) {
        int endIndex = folder.lastIndexOf(".");
        config.setProjectName(folder.substring(0, endIndex));
        found = true;
        break;
      }
    }

    if (!found) {
      System.err.println("Failed to find project name from export java files.");
      System.exit(1);
    }
  }
}
