package checks.junit;

import main.Config;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import reporting.ReportMaker;
import reporting.TestResult;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * This class is responsible for running the JUNIT tests that has been passed in by the user. It
 * will compile all of the exported processing code along with the JUNIT tests and run them against
 * all Class files.
 */
public class JUnitRunner {

  public JUnitRunner() {}

  /**
   * This function is responsible for moving the test file into the java source directory to then be
   * compiled.
   *
   * @param config the config class which holds all needed config for the application.
   * @return
   */
  private String moveTestFileIntoSourceDirectory(Config config) {
    String testFileLocation = config.getJunitLocation();

    // Copy the test file into the source directory.
    File testFile = new File(testFileLocation);
    String testFileName = testFile.getName();

    File dest = new File(config.getTempLocation() + "/source/" + testFileName);
    try {
      Files.copy(testFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Failed to move the test file into the java source directory.");
      return "";
    }

    return dest.toPath().toString();
  }

  /**
   * This function is responsible for compiling the JUNIT code along with the Java code. It will
   * then run the JUNIT tests/
   *
   * @param config the config class which holds all needed config for the application.
   * @param testFile location of the JUNIT test file.
   * @return returns the JUNIT result object.
   */
  private Result compileAndRunJUnitTests(Config config, String testFile) {
    // Compile all the files in the directory.
    File[] files;
    File classDir = new File(config.getTempLocation() + "/source/");
    files =
        classDir.listFiles(
            new FilenameFilter() {
              @Override
              public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".java");
              }
            });

    String[] fileNames = new String[files.length];
    for (int i = 0; i < files.length; i++) {
      fileNames[i] = files[i].getAbsolutePath();
    }

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    int compilerResult = compiler.run(null, null, null, fileNames);
    if (compilerResult != 0) {
      System.out.println("Error compiler result code: " + compilerResult);
      return null;
    }

    String testFileWithoutExtension = new File(testFile).getName().replaceFirst("[.][^.]+$", "");

    Result result = null;
    try {
      URL url = classDir.toURI().toURL();
      URL[] urls = {url};
      ClassLoader classLoader = new URLClassLoader(urls);

      Class<?> junitTest = Class.forName(testFileWithoutExtension, true, classLoader);
      result = JUnitCore.runClasses(junitTest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      System.err.println("Failed to compile JUNIT tests.");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.err.println("Failed to compile JUNIT tests.");
    }

    return result;
  }

  /**
   * This function will take in the JUNIT result object and will output the results to the command
   * line and will also calculate the final grade for this module.
   *
   * @param result the JUNIT result object.
   * @return returns the score calculated from the test results.
   */
  private float parseJUnitResults(Result result) {
    return ((float)result.getRunCount() - (float)result.getFailureCount()) / (float)result.getRunCount();
  }

  /**
   * The function responsible for calling the other methods in the class.
   *
   * @param config the config class which holds all needed config for the application.
   */
  public void runJUNITTests(Config config) {
    String testFileLocation = moveTestFileIntoSourceDirectory(config);

    if (testFileLocation == "") {
      System.err.println("Skipping JUNIT tests due to error.");
      return;
    }

    Result result = compileAndRunJUnitTests(config, testFileLocation);
    if (result == null) {
      System.err.println("Failed to compile the Java classes.");
      return;
    }
    float percentageResult = parseJUnitResults(result) * 100;

    // Test 1 details here
    TestResult testResult = new TestResult();
    testResult.name = "JUNIT Tests";
    testResult.desc = "Result of JUnit tests passed into the program.";
    testResult.value = String.valueOf(percentageResult);
    testResult.result = (percentageResult >= 50) ? true : false;
    testResult.numberResult = true;
    testResult.testOutput = "";
    testResult.testOutput += "Number of Tests Run: " + result.getRunCount() + " ";
    testResult.testOutput += "Number of Failures: " + result.getFailureCount() + " ";
    testResult.testOutput += "Number of Ignored Tests: " + result.getIgnoreCount() + " ";
    testResult.testOutput += "Total Test Runtime: " + result.getRunTime() + " seconds ";
    testResult.testOutput += "Successful: " + result.wasSuccessful() + " ";

    if (result.getFailureCount() > 0) {
      testResult.testOutput += "Failures: ";
      for (Failure failure : result.getFailures()) {
        testResult.testOutput += failure.toString() + " ";
      }
    }

    ReportMaker.addDataToReport(testResult);
  }
}
