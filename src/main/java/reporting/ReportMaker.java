package reporting;

// Assistance from https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for creating the CSV based on an array of TestResults.
 */

public class ReportMaker {
  public static String STUDENT_ID;
  public static ArrayList<TestResult> results = new ArrayList<TestResult>();

  public static void addDataToReport(TestResult tRO) {
    results.add(tRO);
  }

  public static void addDataToCSV(String outputFile) {
    // case where not valid by *this point* but ¯\_(ツ)_/¯
    File file = new File(outputFile);
    try {
      FileWriter fileWriter = new FileWriter(outputFile);

      CSVWriter writer =
          new CSVWriter(
              fileWriter,
              ' ',
              CSVWriter.NO_QUOTE_CHARACTER,
              CSVWriter.DEFAULT_ESCAPE_CHARACTER,
              CSVWriter.DEFAULT_LINE_END); //  remove "garbage" from .csv file

      List<String[]> data =
          new ArrayList<String[]>(); // list which maintains our data to be written
      int noOfRow = results.size();
      String SID = ("Student ID: " + STUDENT_ID);
      String[] SIDINFO = SID.split(" ");
      data.add(SIDINFO);

      for (int i = 0; i < noOfRow; i++) {
        // collect data from testResultObject(s)
        String row = (results.get(i).returnFormatted());
        String[] rowdata = row.split(" ");
        data.add(rowdata);
      }

      writer.writeAll(data); // write all "data" to CSV

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
