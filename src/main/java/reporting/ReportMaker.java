package reporting;

// Assistance from https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** This class is responsible for creating the CSV based on an array of TestResults. */
public class ReportMaker {
  public static String STUDENT_ID;
  public static ArrayList<TestResult> testResults = new ArrayList<TestResult>();

  public static void addDataToReport(TestResult testResult) {
    testResults.add(testResult);
  }

  public static void addDataToCSV(String outputFile) {
    File file = new File(outputFile);
    try {
      FileWriter fileWriter = new FileWriter(outputFile);

      CSVWriter writer =
          new CSVWriter(
              fileWriter,
              ',',
              CSVWriter.NO_QUOTE_CHARACTER,
              CSVWriter.DEFAULT_ESCAPE_CHARACTER,
              CSVWriter.DEFAULT_LINE_END); //  remove "garbage" from .csv file

      String headers = "Test Name,Description,Value (%),Mark Received,Test Output";
      ArrayList<String[]> csvRows = new ArrayList<>();
      csvRows.add(headers.split(","));

      for(TestResult testResult : testResults) {
        csvRows.add(testResult.returnFormatted());
      }

      writer.writeAll(csvRows); // write all "data" to CSV

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void reset() {
    testResults = new ArrayList<TestResult>();
  }
}
