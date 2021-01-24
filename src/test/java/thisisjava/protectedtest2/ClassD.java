package thisisjava.protectedtest2;

import thisisjava.protectedtest.ClassA;

public class ClassD extends ClassA {

  public ClassD(){
    super();
    this.field = "field";
    this.method();
  }

  public void method(){
//    ClassA a = new ClassA();
//    this.field = "x";
  }
}
