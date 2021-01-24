package thisisjava.object;

import java.util.Optional;

public class MemberExample {

  public static void main(String[] args) {

    Member m1 = new Member("blue");
    Member m2 = new Member("blue");
    Member m3 = new Member("red");

    System.out.println("m1:" + m1);
    System.out.println("m2:" + m2);
    System.out.println("m3:" + m3);

    if (m1.equals(m2)) {
      System.out.println("m1 and m2 are equal.");
    }
    if (m1.equals(m3)) {
      System.out.println("m1 and m3 are equal.");
    } else {
      System.out.println("m1 and m3 are not equal.");
    }
  }
}
