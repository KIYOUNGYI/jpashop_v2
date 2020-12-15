package modern.behaviorparameterization;

public class Apple {

  private int weight;
  private String color;

  protected Apple(){}

  public Apple(int weight, String color) {
    this.weight = weight;
    this.color = color;
  }

  public int getWeight() {
    return weight;
  }

  public String getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "Apple{" +
        "weight=" + weight +
        ", color=" + color +
        '}';
  }
}
