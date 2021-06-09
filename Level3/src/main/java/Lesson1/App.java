package Lesson1;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // 1 задание
        String[] strings = new String[]{"первое", "слово", "дороже", "второго"};
        System.out.println(Arrays.toString(strings));

        Integer[] ints = new Integer[]{1, 2, 3, 4};
        System.out.println(Arrays.toString(ints) + "\n");

        ArrayConverter<String> arrConvStr = new ArrayConverter<>(strings);
        ArrayConverter<Integer> arrConvInt = new ArrayConverter<>(ints);

        arrConvStr.swap(0, 3);
        String[] resultStr = arrConvStr.getArray();
        arrConvInt.swap(0, 3);
        Integer[] resultInt = arrConvInt.getArray();
        System.out.println(Arrays.toString(resultStr));
        System.out.println(Arrays.toString(resultInt) + "\n");

        // 2 задание
        arrConvStr.convertArrayToArrayList();
        List<String> newList = arrConvStr.getList();

        // 3 задание
        Apple apple = new Apple();
        Orange orange = new Orange();

        Box<Apple> boxOfApples = new Box<>();
        for (int i = 0; i < 7; i++) {
            boxOfApples.addFruit(apple);
        }

        Box<Orange> boxOfOranges = new Box<>();
        for (int i = 0; i < 3; i++) {
            boxOfOranges.addFruit(orange);
        }

        Box<Orange> orangeBox = new Box<>();
        for (int i = 0; i < 5; i++) {
            orangeBox.addFruit(orange);
        }

        boolean appleBoxToOrange1Box = boxOfApples.compare(boxOfApples);
        System.out.println(appleBoxToOrange1Box ? "Веса коробок равны" : "Веса коробок не равны");

        System.out.println("Вес коробки с яблоками - " + boxOfApples.getWeight());
        System.out.println("Вес синей коробки с апельсинами - " + boxOfOranges.getWeight());
        System.out.println("Вес красной коробки с апельсинами - " + orangeBox.getWeight() + "\n");

        boxOfOranges.pourToBox(orangeBox);
        System.out.println("Вес синей коробки с апельсинами - " + boxOfOranges.getWeight());
        System.out.println("Вес красной коробки с апельсинами - " + orangeBox.getWeight());
    }
}
