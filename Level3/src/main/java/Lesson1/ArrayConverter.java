package Lesson1;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ArrayConverter<T> {

    private final T[] array;
    private List<T> list;

    public T[] getArray() {
        return array;
    }

    public List<T> getList() {
        return list;
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
        list = new ArrayList<>();
        Collections.addAll(list, array);
    }
}
