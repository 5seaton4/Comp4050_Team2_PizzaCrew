
public class installcheck {

  // check if is installed on computer
  //    public static boolean checkInstall() {
  //        String pathToExecutable =
  // "C:\\Users\\Aashir\\Downloads\\processing-4.0.1\\processing.exe";
  //        RuntimeChecker checker = new RuntimeChecker(pathToExecutable);
  //        return checker.doesExecutableExist();
  //    }

  public static boolean isInstalled() {
    // path needs work
    try {
      Process process = Runtime.getRuntime().exec("processing.exe");
      int code = process.waitFor();
      return code == 0;
    } catch (Exception e) {
      return false;
    }
  }
  // check if is installed on computer
  //   public static void main(String[] args) {
  //       System.out.println(isInstalled());
  //   }

}
