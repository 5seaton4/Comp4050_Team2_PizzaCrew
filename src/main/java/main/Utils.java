package main;

import org.apache.commons.cli.*;
import reporting.ReportMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
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

    Option studentIdOption = new Option("s", "student-id", true, "Student ID");
    studentIdOption.setRequired(false);
    options.addOption(studentIdOption);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

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
    String runRuntimeCheck = cmd.getOptionValue("run-runtime-check");
    String runStaticAnalysis = cmd.getOptionValue("run-static-analysis");

    if ((runStaticAnalysis != null) || (runRuntimeCheck != null)) {
      config.setRunIndividual(true);
    }

    if (runRuntimeCheck != null) {
      config.setRunRuntimeCheck(true);
    }

    if (runStaticAnalysis != null) {
      config.setRunStaticAnalysis(true);
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

  public static void exportProcessingCodeToJava(Config config) {
    System.out.println("Exporting processing code to Java.");

    String platform = "windows";
    if (config.isMac()) {
      platform = "macosx";
    }

    // Create a temp directory for the java code.
    Path tempPath = Paths.get(config.getTempLocation());

    // TODO Remove the temp location if it is already there.
    // TODO Or warn the user what the issue is.

    String[] arguments = {
      config.getProcessingLocation(),
      "--sketch=" + config.getProjectDirectory(),
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

      Utils.getAppNameFromExecutable(config);
    } catch (IOException e) {
      // TODO(Jack): Error handling
      e.printStackTrace();
    }
  }

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
