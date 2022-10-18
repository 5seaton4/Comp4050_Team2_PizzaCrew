package static_export.static_export;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class read {
  public static void main(String args[]) throws IOException {
    Runtime rt = Runtime.getRuntime();
    String[] commands = {"system.exe", "-get t"};
    Process proc = rt.exec(commands);

    BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

    System.out.println("Here is the standard output of the command:\n");
    String s = null;
    while ((s = stdInput.readLine()) != null) {
      System.out.println(s);
    }
  }
}
