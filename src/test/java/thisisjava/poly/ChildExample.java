package thisisjava.poly;

public class ChildExample {

  public static void main(String[] args) {
    Child child = new Child();

    Parent parent = child;//형 변환, 근데
    parent.method1();
    parent.method2();
    child.method1();
  }
}
