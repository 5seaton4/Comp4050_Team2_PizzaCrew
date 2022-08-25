package main;

public class testResultObject {
  String name; // name of JUNIT test (if provided and relevant)
  String desc; // description of JUNIT test (if provided and relevant)
  float value; // mark value of JUNIT test
  float result; // result of test mark, out of 100
  int orderOfAppearance; // appearance order of the test

  public static void main(String[] args) {
    testResultObject tRO = new testResultObject();
    tRO.name = "Object 1 output test";
    tRO.desc = "Description of test";
    tRO.value = 30;
    tRO.result = 63;
    tRO.orderOfAppearance = 1;
    System.out.println(tRO.returnFormatted());
  }

  String returnFormatted() {
    // find a *neat* way of checking all variables have been instantiated before running this
    // method.
    return ("Test: "
        + orderOfAppearance
        + " ; "
        + desc
        + " ; Value: "
        + value
        + "% ; Mark received: "
        + result
        + " ; Contribution to total mark: "
        + result * (value / 100));
  }
}
