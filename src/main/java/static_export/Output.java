package static_export;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.jar.JarException;

public class Output {
  // public static void main(String args[]) throws FileNotFoundException {
  //   try {
  //       File file =
  //           new File("C:/Users/abhin/Documents/COMP4050Pizza/Comp4050_Team2_PizzaCrew/results.xls");
  //       Scanner scan = new Scanner(file);

  //       while (scan.hasNextLine()) {
  //       System.out.println("Hi");
  //       System.out.println(scan.nextLine());
  //       }   
  //   }
  //   catch (FileNotFoundException ex) {
  //       ex.printStackTrace();
  //   }
  //   public static void main(String args[]) {
  //     System.out.println("my name is abhinav");
  // }

  public static void main(String args[]) throws FileNotFoundException{
    try {
      File file = new File("C:/Users/abhin/Documents/Ouputs/file.txt");
      Scanner scan = new Scanner(file);
      System.out.println(scan.nextLine());
    }
    catch(FileNotFoundException e) {
      System.out.println("error");
    }
  }
}

