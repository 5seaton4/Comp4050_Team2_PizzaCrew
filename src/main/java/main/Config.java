package main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * This class holds all the configuration needed by all the test modules. It includes static
 * variables but also variables calculated at runtime and some helper functions.
 */
public class Config {
  // The location of processing-java
  private String processingLocation;
  // The location of the project being tested.
  private String projectDirectory;

  // The location of the JAR file currently being run.
  private String jarLocation;

  // The location of the temp directory.
  private String tempLocation;
  // The location of the static analysis tool PMD.
  private String staticAnalysisLocation;
  // The location of the JUNIT tests.
  private String junitLocation;
  // The project name that is being tested.
  private String projectName;

  // The default location where the results are stored. This will change if multiple projects are
  // run.
  public String resultsCSVLocation = "./Results/results.csv";

  // Are we running individual tests or all?
  private boolean runIndividual;
  private boolean runRuntimeCheck;
  private boolean runStaticAnalysis;

  // Are we testing multiple projects.
  private boolean runMultiple;

  // The location of the parent temp directory.
  private static final String TEMP_DIRECTORY_NAME = "exported-processing-code";
  // The static analysis relative location to the JAR file.
  private static final String STATIC_ANALYSIS_RELATIVE_LOCATION = "tools/pmd-bin-6.49.0/bin/";

  // Supported OS types.
  enum OS_TYPE {
    WINDOWS,
    MAC,
  }

  private OS_TYPE platform;

  public Config() {}

  /** Creates the results directory if it does not exist already. */
  public void createResultsDirectory() {
    File dir = new File("./Results");
    if (!dir.isDirectory()) {
      dir.mkdir();
    }
  }

  /** Calculate the working directory from the location of the JAR file. */
  public void getWorkingDirectory() {
    // Get directory of executable, which we can then use to extrapolate where the dependencies are.
    String path = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    try {
      String decodedPath = URLDecoder.decode(path, "UTF-8");
      int endIndex = decodedPath.lastIndexOf("/");

      String workingDir = decodedPath.substring(0, endIndex);

      // Windows doesn't behave with the path calculated above.
      if (isWindows()) {
        workingDir = workingDir.substring(1);
      }

      setJarLocation(workingDir);

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  /** Checks that the OS the user is on is compatible. */
  public void checkOS() {
    String os = System.getProperty("os.name");
    if (os.contains("Mac")) {
      setPlatform(OS_TYPE.MAC);
    } else if (os.contains("Windows")) {
      setPlatform(OS_TYPE.WINDOWS);
    } else {
      System.err.println("Operating System not supported.");
      System.exit(1);
    }
  }

  /** Remove the temporary folder that we calculated previously. */
  public void removeTemporaryFolder() {
    removeFolderRecursively(new File(this.tempLocation));
  }

  /**
   * This function is used to remove the temporary folder.
   *
   * @param directoryToBeDeleted the directory tha will be deleted.
   */
  private void removeFolderRecursively(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        removeFolderRecursively(file);
      }
    }

    directoryToBeDeleted.delete();
  }

  public String getJunitLocation() {
    return this.junitLocation;
  }

  public void setJunitLocation(String junitLocation) {
    this.junitLocation = junitLocation;
  }

  public String getStaticAnalysisLocation() {
    return this.staticAnalysisLocation;
  }

  public String getProjectName() {
    return this.projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public boolean isMac() {
    return platform == OS_TYPE.MAC;
  }

  public boolean isWindows() {
    return platform == OS_TYPE.WINDOWS;
  }

  public String getTempLocation() {
    return this.tempLocation;
  }

  /**
   * Sets the JAR location and then sets other config variables that are based on the JAR location.
   *
   * @param jarLocation the location of the JAR file that is being run.
   */
  public void setJarLocation(String jarLocation) {
    this.jarLocation = jarLocation;
    this.tempLocation = this.jarLocation + "/" + TEMP_DIRECTORY_NAME;

    String staticAnalysisLocationBuilder =
        this.jarLocation + "/" + STATIC_ANALYSIS_RELATIVE_LOCATION;

    if (isMac()) {
      staticAnalysisLocationBuilder += "run.sh";
    } else if (isWindows()) {
      staticAnalysisLocationBuilder += "pmd.bat";
    }

    this.staticAnalysisLocation = staticAnalysisLocationBuilder;
  }

  public String getJarLocation() {
    return this.jarLocation;
  }

  public void setProcessingLocation(String location) {
    this.processingLocation = location;
  }

  public String getProcessingLocation() {
    return this.processingLocation;
  }

  public void setProjectDirectory(String location) {
    this.projectDirectory = location;
  }

  public String getProjectDirectory() {
    return this.projectDirectory;
  }

  public void setPlatform(OS_TYPE platform) {
    this.platform = platform;
  }

  public OS_TYPE getPlatform() {
    return this.platform;
  }

  public boolean isRunStaticAnalysis() {
    return runStaticAnalysis;
  }

  public void setRunStaticAnalysis(boolean runStaticAnalysis) {
    this.runStaticAnalysis = runStaticAnalysis;
  }

  public boolean isRunRuntimeCheck() {
    return runRuntimeCheck;
  }

  public void setRunRuntimeCheck(boolean runRuntimeCheck) {
    this.runRuntimeCheck = runRuntimeCheck;
  }

  public boolean isRunIndividual() {
    return runIndividual;
  }

  public void setRunIndividual(boolean runIndividual) {
    this.runIndividual = runIndividual;
  }

  public boolean isRunMultiple() {
    return runMultiple;
  }

  public void setRunMultiple(boolean runMultiple) {
    this.runMultiple = runMultiple;
  }

  public void setTempLocation(String tempLocation) {
    this.tempLocation = tempLocation;
  }

  public String getResultsCSVLocation() {
    return resultsCSVLocation;
  }

  public void setResultsCSVLocation(String resultsCSVLocation) {
    this.resultsCSVLocation = resultsCSVLocation;
  }
}
