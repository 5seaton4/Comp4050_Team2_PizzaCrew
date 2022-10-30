package reporting;

// Assistance from https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** This class is responsible for creating the CSV based on an array of TestResults. */
public class ReportMaker {
  public static String STUDENT_ID;
  public static ArrayList<TestResult> results = new ArrayList<TestResult>();
  public static ArrayList<String> SAResults = new ArrayList<String>();
  public static boolean runtimeresult;
  public static ArrayList<String> failurecodes = new ArrayList<String>();

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

      // Setup csv and add student ID
      List<String[]> data =
          new ArrayList<String[]>(); // list which maintains our data to be written
      int noOfRow = results.size();
      String SID = ("Student ID: , " + STUDENT_ID);
      String[] SIDINFO = SID.split(" ");
      data.add(SIDINFO);

      // runtime results printed on one line, no need for anything beyond title
      String RTTResult;
      if (runtimeresult) {
        RTTResult = "Pass";
      } else {
        RTTResult = "Fail";
      }
      String title = "Runtime test result: ," + RTTResult;
      String[] titleadd = title.split(" ");
      data.add(titleadd);

      title = "Static Analysis Result: ";
      titleadd = title.split(" ");
      data.add(titleadd);

      // Static analysis printing
      // todo what happens if the static analysis has a comma in it? This will ruin .csv formatting
      // :(
      for (int i = 0; i < SAResults.size() - 1; i++) {
        if (i
            > 11) { // skips import statements as these are made by processing-java and not relevant
          // to student
          String row = (SAResults.get(i));
          String[] rowdata = row.split(" ");
          data.add(rowdata);
        }
      }

      title = "Unit Tests Results: ";
      titleadd = title.split(" ");
      data.add(titleadd);
      int addscore = 0;
      // Junit printing
      for (int i = 0; i < noOfRow; i++) {
        // collect data from testResultObject(s)
        String row = (results.get(i).returnFormatted());
        String[] rowdata = row.split(" ");
        data.add(rowdata);
        if (results.get(i).result) {
          addscore += results.get(i).value;
        }
      }
      String score = "Unit test mark: , ";
      score += String.valueOf(addscore);
      titleadd = score.split(" "); // using titleadd over new var as to save space
      data.add(titleadd);

      if (failurecodes.size() > 0) {
        for (int y = 0; y < failurecodes.size(); y++) {
          titleadd = failurecodes.get(y).split(" ");
          data.add(titleadd);
        }
      }

      writer.writeAll(data); // write all "data" to CSV

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
