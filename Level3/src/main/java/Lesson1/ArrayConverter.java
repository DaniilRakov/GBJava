package Lesson1;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayConverter<T> {

    private final T[] array;
    private ArrayList<T> arrayList;

    public T[] getArray() {
        return array;
    }

    public ArrayList<T> getArrayList() {
        return arrayList;
    }

    public ArrayConverter(T[] array) {
        this.array = array;
    }

    public void swap(int first, int second) {
        T temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    public void convertArrayToArrayList() {
        arrayList = new ArrayList<>();
        Collections.addAll(arrayList, array);
    }
}
