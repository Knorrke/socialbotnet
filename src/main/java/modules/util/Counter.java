package modules.util;

public class Counter {

  private static volatile int id = 0;

  public static synchronized int nextInt() {
    return ++id;
  }
}
