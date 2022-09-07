package main;

public class JUnitHelper {

  static testResultObject TRO = new testResultObject();

  static testResultObject templateToTRO() {
    TRO.orderOfAppearance = 1;
    TRO.value = 30;
    TRO.desc = "Test 1 description";
    TRO.name = "Test 1";
    TRO.result = JUnitFile.testtemplate();
    return TRO;
  }

  public static void main(String[] args) {
    reportMaker.addDataToReport(templateToTRO());
    reportMaker.addDataToCSV("./testresult.csv");
  }
}
