package Lesson1;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String[] strings = new String[]{"первое", "слово", "дороже", "второго"};
        System.out.println(Arrays.toString(strings));

        Integer[] ints = new Integer[]{1, 2, 3, 4};
        System.out.println(Arrays.toString(ints) + "\n");

        ArrayConverter<String> arrConvStr = new ArrayConverter<>(strings);
        ArrayConverter<Integer> arrConvInt = new ArrayConverter<>(ints);

        arrConvStr.convertArrayToArrayList();
        List<String> outList = arrConvStr.getArrayList();

        arrConvStr.swap(0, 3);
        String[] resultStr = arrConvStr.getArray();
        arrConvInt.swap(0, 3);
        Integer[] resultInt = arrConvInt.getArray();
        System.out.println(Arrays.toString(resultStr));
        System.out.println(Arrays.toString(resultInt) + "\n");
    }
}
