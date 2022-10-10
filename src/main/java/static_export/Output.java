package static_export;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Output {
  public static void main(String args[]) throws FileNotFoundException {
    File file =
        new File("C:/Users/abhin/Documents/COMP4050Pizza/Comp4050_Team2_PizzaCrew/data1.txt");
    Scanner scan = new Scanner(file);

    while (scan.hasNextLine()) {
      System.out.println("Hi");
      System.out.println(scan.nextLine());
    }
  }
}
