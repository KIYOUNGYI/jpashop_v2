package modern.behaviorparameterization;

import static modern.behaviorparameterization.Color.GREEN;
import static modern.behaviorparameterization.Color.RED;

import java.util.ArrayList;
import java.util.List;

/**
 * 전략 패턴
 */
public class AppleTestMain {

  public static void main(String[] args) {
    Apple apple = new Apple(60, GREEN.toString());
    Apple apple2 = new Apple(40, RED.toString());
    Apple apple3 = new Apple(30, GREEN.toString());
    Apple apple4 = new Apple(90, GREEN.toString());
    List<Apple> inventory = new ArrayList<>();
    inventory.add(apple);
    inventory.add(apple2);
    inventory.add(apple3);
    inventory.add(apple4);
    List<Apple> apples = filterApples(inventory, new AppleHeavyWeightPredicate());
    System.out.println("> apple : " + apples);

  }

  public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate applePredicate) {

    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
      if (applePredicate.test(apple)) {
        result.add(apple);
      }
    }
    return result;
  }
}
