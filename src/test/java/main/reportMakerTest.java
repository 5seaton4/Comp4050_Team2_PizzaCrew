package main;

// Assistance from https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/

import com.opencsv.CSVWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// todo current worst case is that StudentID or results are NULL -> prints "NULL"
public class reportMakerTest {
  static List JUnitAssistanceVariable;
  static String StudentID;
  static ArrayList<testResultObject> results = new ArrayList<testResultObject>();
  private static final String CSV_FILE_PATH =
      "./result.csv"; // todo discuss appropriate filepath for result.csv

  public static void main(String[] args) {
    addDataToCSV(CSV_FILE_PATH);
  }

  public static void addDataToReport(
      testResultObject
          tRO) { // todo is this appropriate or should we reference "results.add" statically? TBD
    results.add(tRO);
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

      List<String[]> data =
          new ArrayList<String[]>(); // list which maintains our data to be written
      int noOfRow = results.size();
      String SID = ("Student ID: " + StudentID);
      String[] SIDINFO = SID.split(" ");
      data.add(SIDINFO);

      for (int i = 0; i < noOfRow; i++) {
        // collect data from testResultObject(s)
        String row =
            ("Test: "
                + results.get(i).orderOfAppearance
                + " ; "
                + results.get(i).desc
                + " ; Value: "
                + results.get(i).value
                + "% ; Mark received: "
                + results.get(i).result
                + " ; Contribution to total mark: "
                + results.get(i).result * (results.get(i).value / 100));
        String[] rowdata = row.split(" ");
        data.add(rowdata);
      }

      writer.writeAll(data); // write all "data" to CSV
      JUnitAssistanceVariable = data;

      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void returnReport() {
    testResultObject test1 = new testResultObject();
    StudentID = "45956022";
    test1.name = "Object 1 output test";
    test1.desc = "Description of test";
    test1.value = 30;
    test1.result = 63;
    test1.orderOfAppearance = 1;
    addDataToReport(test1);
    addDataToCSV("./testresult.csv");
    // todo convert from JUnitAssistanceVariable (list) to something comparable to our results
    // (convert it to string or create our own list?)

    assertEquals("Test", "Test");
  }
}
