package checks.runtime;

import main.Config;
import reporting.ReportMaker;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for checking the runtime attributes of the application being tested.
 * The application will be run and the exit code checked for any runtime errors.
 *
 * @author Jack Seaton
 * @version 1.0
 * @since 2022-08-23
 */
public class RuntimeChecker {

  // In milliseconds - 4000 ms = 3s
  private static int PROGRAM_RUN_TIME = 10000;

  // The relative location of the executable being tested.
  private String pathToExecutable;

  public RuntimeChecker() {}

  /**
   * This method will check for the existence of the executable.
   *
   * @params None
   * @return boolean Returns whether or not the executable exists.
   */
  public boolean doesExecutableExist() {
    File executable = new File(pathToExecutable);
    // Will check that it exists and that it is a file.
    if (executable.isFile()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * This method will run the executable for 3 seconds, if the executable crashes the exitcode is
   * recorded. The results are stored in a helper class RuntimeCheckResult which will be returned
   * from the function.
   *
   * @params None
   * @return boolean Returns true or false based on whether the application crashed in under 3
   *     seconds.
   */
  public boolean runExecutable(Config config) {
    if (config.isMac()) {
      // We need to get the name of the .app file to then know the name of the executable.
      // We then traverse that .app/Contents/MacOS/<AppName>
      pathToExecutable =
          config.getTempLocation()
              + "/"
              + config.getProjectName()
              + ".app/"
              + "Contents/MacOS/"
              + config.getProjectName();

    } else if (config.isWindows()) {
      pathToExecutable = config.getTempLocation() + "/" + config.getProjectName() + ".exe";
    }

    if (!doesExecutableExist()) {
      System.err.println("Error - Executable does not exist.");
      System.exit(1);
    }

    boolean success = true;

    try {
      Runtime runtime = Runtime.getRuntime();
      // Run the executable.
      Process process = runtime.exec(pathToExecutable);

      try {
        // Sleep the thread for 3 seconds, to allow the program to execute.
        Thread.sleep(PROGRAM_RUN_TIME);
      } catch (InterruptedException e) {
        // Thread has been interrupted, something has gone wrong.
        success = false;
        e.printStackTrace();
      }

      process.destroy();
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    ReportMaker.runtimeresult = success;
    return success;
  }
}
