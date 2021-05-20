package Lesson3;

import java.util.*;

public class PhoneBook {
    private final Map<String, List<String>> phoneBook;

    public PhoneBook() {
        this.phoneBook = new HashMap<>();
    }

    public void add(String surname, String phoneNumber) {
        List<String> list = phoneBook.getOrDefault(surname, new ArrayList<>());

        list.add(phoneNumber);
        phoneBook.put(surname, list);
    }

    public List<String> get(String surname) {
        return phoneBook.getOrDefault(surname, Collections.emptyList());
    }
}
