package main;

import java.util.ArrayList;
import java.util.List;

public class App {

  public App() {
    System.out.println("Constructor...");
  }

  public void m1() {
    String s = "";
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list = null;
    list.add(2);
    Object obj = getData();
    System.out.println(obj.toString());
  }

  public Object getData() {
    return null;
  }

  public static void main(String[] args) {
    System.out.println("Hello World");
    App app = new App();
    app.m1();
  }
}
