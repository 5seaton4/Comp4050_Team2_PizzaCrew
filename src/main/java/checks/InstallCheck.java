public class InstallCheck {
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
}
