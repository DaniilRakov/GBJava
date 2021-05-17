package Lesson5;

import java.util.Arrays;

public class Main {

    private static final int SIZE = 10000000;
    private static final int HALF_OF_SIZE = SIZE / 2;

    public static void main(String[] args) throws InterruptedException {
        firstMethod();
        secondMethod();
    }

    private static void firstMethod() {
        float[] array = new float[SIZE];
        Arrays.fill(array, 1);
        long timeStart = System.currentTimeMillis();

        for (int i = 0; i < SIZE; i++)
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

        long timeEnd = System.currentTimeMillis();
        System.out.println("Время работы первого метода: " + (timeEnd - timeStart));
    }

    private static void secondMethod() throws InterruptedException {
        float[] fullArray = new float[SIZE];
        Arrays.fill(fullArray, 1);
        long timeStart = System.currentTimeMillis();

        final float[] firstArray = new float[HALF_OF_SIZE];
        final float[] secondArray = new float[HALF_OF_SIZE];
        System.arraycopy(fullArray, 0, firstArray, 0, HALF_OF_SIZE);
        System.arraycopy(fullArray, HALF_OF_SIZE, secondArray, 0, HALF_OF_SIZE);

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < HALF_OF_SIZE; i++)
                    firstArray[i] = (float) (firstArray[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                for (int i = HALF_OF_SIZE; i < SIZE; i++)
                    secondArray[i - HALF_OF_SIZE] = (float) (secondArray[i - HALF_OF_SIZE] * Math.sin(0.2f + i / 5) *
                            Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });


        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.arraycopy(firstArray, 0, fullArray, 0, HALF_OF_SIZE);
        System.arraycopy(secondArray, 0, fullArray, HALF_OF_SIZE, HALF_OF_SIZE);

        long timeEnd = System.currentTimeMillis();
        System.out.println("Время работы второго метода: " + (timeEnd - timeStart));
    }
}
