package reporting;

// Assistance from https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// todo current worst case is that StudentID or results are NULL -> prints "NULL"
public class ReportMaker {
  public static String STUDENT_ID;
  public static ArrayList<TestResult> results = new ArrayList<TestResult>();
  public static ArrayList<String> SAResults = new ArrayList<String>();
  public static boolean runtimeresult;
  public static ArrayList<String> failurecodes = new ArrayList<String>();

  private static final String CSV_FILE_PATH =
      "./result.csv"; // todo discuss appropriate filepath for result.csv

  public static void addFailureReasons(String failreason) {
    failurecodes.add(failreason);
  }
  public static void addDataToReport(
      TestResult
          tRO) { // todo is this appropriate or should we reference "results.add" statically? TBD
    results.add(tRO);
  }

  public static void addSA(String saline) {
    SAResults.add(saline);
  }

  public static void addDataToCSV(
      String
          output) { // todo check validity of "results" before running this method? Shouldn't be a
    // case where not valid by *this point* but ¯\_(ツ)_/¯
    File file = new File(output);
    try {
      FileWriter outputfile = new FileWriter(file);

      CSVWriter writer =
          new CSVWriter(
              outputfile,
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
        for (int y = 0 ; y<failurecodes.size();y++){
          titleadd = failurecodes.get(y).split(" ");
          data.add(titleadd);
        }
      }

      writer.writeAll(data); // write all "data" to CSV

      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
