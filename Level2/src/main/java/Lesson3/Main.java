package Lesson3;

import java.util.*;

public class Main {

    private static final int WORDS_COUNT = 10;

    public static void main(String[] args) {
        // ------------------------- 1 ---------------------
        String[] words = new String[WORDS_COUNT];
        Arrays.fill(words, "word");
        words[2] = "ball";

        Map<String, Integer> uniqueWords = getUniqueWords(words);

        for (Map.Entry<String, Integer> word :
                uniqueWords.entrySet()) {
            System.out.println("Слово - " + word.getKey() + ", количество повторений - " + word.getValue());
        }
        System.out.println();

        // -------------------------- 2 -----------------------
        PhoneBook phoneBook = new PhoneBook();
        String surname = "Иванов";
        String surname1 = "Иванов";
        String surname2 = "Петров";
        String surname3 = "Сидоров";
        phoneBook.add(surname, "8 (012) 345-67-89");
        phoneBook.add(surname1, "8 (123) 456-78-90");
        phoneBook.add(surname2, "8 (234) 567-89-01");
        phoneBook.add(surname3, "8 (345) 678-90-12");

        List<String> phoneNumbers = phoneBook.get(surname);
        System.out.println("Фамилия - " + surname);
        for (String phoneNumber : phoneNumbers) {
            System.out.println("Телефонный номер - " + phoneNumber);
        }
    }

    private static Map<String, Integer> getUniqueWords(String[] inputArray) {
        Map<String, Integer> outMap = new HashMap<>();

        for (String surname : inputArray)
            outMap.put(surname, outMap.getOrDefault(surname, 0) + 1);

        return outMap;
    }
}
