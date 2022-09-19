package main;

public class JUnitFile {

  public static boolean test1() {
    if (1 == 0) {
      return false;
    }
    return true;
  }

  public static boolean test2() {
    return false;
  }
}
