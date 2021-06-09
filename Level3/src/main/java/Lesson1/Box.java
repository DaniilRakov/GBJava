package Lesson1;

import java.util.List;
import java.util.ArrayList;

public class Box<T extends Fruit> {
    private final List<T> fruits;

    public Box() {
        fruits = new ArrayList<>();
    }

    public void addFruit(T fruit) {
        fruits.add(fruit);
    }

    public float getWeight() {
        return fruits.size() > 0 ? (long) fruits.size() * fruits.get(0).getWeight() : 0;
    }

    public boolean compare(Box<? extends Fruit> box) {
        return this.getWeight() == box.getWeight();
    }

    public void pourToBox(Box<T> box) {
        fruits.forEach(box::addFruit);
        while (fruits.size() > 0)
            fruits.remove(0);
    }
}
