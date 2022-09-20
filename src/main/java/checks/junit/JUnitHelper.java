package checks.junit;

public class JUnitHelper {
  int noTests = 2; // FIXME - User to input the number of tests into this variable

  static void test1(TestResultObject TRO) {
    TRO.orderOfAppearance = 1;
    TRO.value = 30;
    TRO.desc = "Test 1 description";
    TRO.name = "Test 1";
    TRO.result = JUnitFile.test1();
  }

  static void test2(TestResultObject TRO) {
    TRO.orderOfAppearance = 2;
    TRO.value = 70;
    TRO.desc = "Test 2 description";
    TRO.name = "Test 2";
    TRO.result = JUnitFile.test2();
  }

  static void runtests(TestResultObject TRO1, TestResultObject TRO2) {
    test1(TRO1);
    test2(TRO2);
  }
}
