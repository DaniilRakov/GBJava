package Lesson2;

public class Main {

    public static void main(String[] args) {
        String[][] array = new String[4][4];
        //String[][] array = new String[4][4];
        int a = 1;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = String.valueOf(a);
                a++;
            }
        }

        //array[1][1] = "a";
        try {
            int sum = processArray(array);
            System.out.println("Сумма чисел равна " + sum);
        } catch (MyArraySizeException e) {
            e.printStackTrace(System.out);
        } catch (MyArrayDataException e) {
            e.printStackTrace(System.out);
        }
    }

    private static int processArray(String[][] array) throws MyArraySizeException {
        if (array.length != 4)
            throw new MyArraySizeException("Некорректный размер массива - " + array.length + " строк");
        for (String[] strings : array) {
            if (strings.length != 4)
                throw new MyArraySizeException("Некорректный размер массива - " + strings.length + " столбцов");
        }

        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                } catch (Exception e) {
                    throw new MyArrayDataException("В ячейке с координатами " + i + ","
                            + j + " находятся некорректные данные");
                }
            }
        }

        return sum;
    }
}
