package thisisjava.poly;

public class Main {

  public static void main(String[] args) {
    Cat cat = new Cat();
    System.out.println("cat:"+cat);
    Animal animal = cat;
    System.out.println("animal:"+animal);
    System.out.println(cat==animal);

    Animal animal1 = new Cat();
    System.out.println("animal1:"+animal1);
  }
}
