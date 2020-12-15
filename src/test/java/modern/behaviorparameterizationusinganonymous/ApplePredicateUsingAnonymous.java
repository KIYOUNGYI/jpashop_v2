package modern.behaviorparameterizationusinganonymous;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplePredicateUsingAnonymous {

  public static void main(String[] args) {

    List<Apple> inventory = Arrays.asList(new Apple(80, Color.GREEN.name()),
        new Apple(80, Color.RED.name()),
        new Apple(40, Color.GREEN.name()),
        new Apple(100, Color.RED.name())
    );
    List<Apple> redApples = filter(inventory, (Apple apple) -> Color.RED.name().equals(apple.getColor()));
    System.out.println("redApples -> " + redApples.toString());
  }

  public static <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> result = new ArrayList<>();
    for (T e : list) {
      if (p.test(e)) {
        result.add(e);
      }
    }
    return result;
  }
}
