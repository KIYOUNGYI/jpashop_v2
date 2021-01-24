package thisisjava.object;

public class StringAdressCheckMain {

  public static void main(String[] args) {
    String x = "1";
    String y = "1";

    System.out.println("x->" + Integer.toHexString(x.hashCode()));
    System.out.println("y->" + Integer.toHexString(y.hashCode()));

    if (x == y) {
      System.out.println("yes");
    }
    if (x.equals(y)) {
      System.out.println("hell yeah");
    }
  }
}
