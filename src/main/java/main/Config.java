package main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Config {
  private String processingLocation;
  private String projectDirectory;

  private String jarLocation;
  private String tempLocation;
  private String staticAnalysisLocation;
  private String projectName;

  private static final String TEMP_DIRECTORY_NAME = "exported-processing-code";
  private static final String STATIC_ANALYSIS_RELATIVE_LOCATION = "tools/pmd-bin-6.49.0/bin/";
  public static final String RESULT_CSV_LOCATION = "./results.csv";

  enum OS_TYPE {
    WINDOWS,
    MAC,
    // TODO currently not supported.
    LINUX
  }

  private OS_TYPE platform;

  public Config() {}

  public void getWorkingDirectory() 
  {
   // Get directory of executable, which we can then use to extrapolate where the dependencies are.
    String path = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    try {
      String decodedPath = URLDecoder.decode(path, "UTF-8");
      int endIndex = decodedPath.lastIndexOf("/");

      String workingDir = decodedPath.substring(0, endIndex);

      //Windows doesn't behave with the path calculated above.
      if (isWindows()) {
        workingDir = workingDir.substring(1);    
      }
      
      setJarLocation(workingDir);
  
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

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

  public void removeTemporaryFolder() {
    removeFolderRecursively(new File(this.tempLocation));
  }

  private void removeFolderRecursively(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        removeFolderRecursively(file);
      }
    }

    directoryToBeDeleted.delete();
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

  public void setJarLocation(String jarLocation) {
    this.jarLocation = jarLocation;
    this.tempLocation = this.jarLocation + "/" + TEMP_DIRECTORY_NAME;

    String staticAnalysisLocationBuilder = this.jarLocation + "/" + STATIC_ANALYSIS_RELATIVE_LOCATION;
    
    //TODO this should be moved into constants.
    if(isMac())
    {
      staticAnalysisLocationBuilder += "run.sh";
    }
    else if(isWindows())
    {
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
}
