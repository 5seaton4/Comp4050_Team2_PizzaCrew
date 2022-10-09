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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

// TODO(Jack): Does each processing project have a file with the project as its name?
// TODO(Jack): Make sure this runs on Windows.
// TODO(Jack): Check failures output.
public class JUnitRunner {

  public JUnitRunner() {}

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
    }

    return dest.toPath().toString();
  }

  private Result compileAndRunJUnitTests(Config config, String testFile, String classToTest) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    int compilerResult = compiler.run(null, null, null, testFile, classToTest);
    System.out.println("Compiler result code: " + compilerResult);

    File classDir = new File(config.getTempLocation() + "/source/");
    String testFileName = new File(testFile).getName().replaceFirst("[.][^.]+$", "");

    Result result = null;
    try {
      URL url = classDir.toURI().toURL();
      URL[] urls = {url};
      ClassLoader classLoader = new URLClassLoader(urls);

      Class<?> junitTest = Class.forName(testFileName, true, classLoader);
      result = JUnitCore.runClasses(junitTest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return result;
  }

  private float parseJUnitResults(Result result) {
    System.out.println("JUNIT Tests Have Run.");
    System.out.println("Number of Tests Run: " + result.getRunCount());
    System.out.println("Number of Failures: " + result.getFailureCount());
    System.out.println("Number of Ignored Tests: " + result.getIgnoreCount());
    System.out.println("Total Test Runtime: " + result.getRunTime() + " seconds");
    System.out.println("Successful: " + result.wasSuccessful());

    if (result.getFailureCount() > 0) {
      System.out.println("Failures: ");
      for (Failure failure : result.getFailures()) {
        System.out.println(failure.toString());
      }
    }

    return (result.getRunCount() - result.getFailureCount()) / result.getRunCount();
  }

  public void runJUNITTests(Config config) {
    String testFileLocation = moveTestFileIntoSourceDirectory(config);
    String classToTest = config.getTempLocation() + "/source/" + config.getProjectName() + ".java";

    Result result = compileAndRunJUnitTests(config, testFileLocation, classToTest);
    if (result == null) {
      System.out.println("Failed to compile the Java classes.");
      System.exit(1);
    }

    float percentageResult = parseJUnitResults(result);

    TestResult testResult = new TestResult();
    testResult.name = "JUNIT Tests";
    testResult.desc = "Result of JUnit tests passed into the program.";
    testResult.value = percentageResult;
    testResult.result = (percentageResult >= 50) ? true : false;
    ReportMaker.addDataToReport(testResult);
  }
}
