package Lesson3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhoneBook {
    private Map<String, ArrayList<String>> phoneBook;

    public PhoneBook() {
        this.phoneBook = new HashMap<String, ArrayList<String>>();
    }

    public void add(String surname, String phoneNumber) {
        ArrayList<String> list;
        if (phoneBook.containsKey(surname)) {
            list = phoneBook.get(surname);
        } else {
            list = new ArrayList<String>();
        }
        list.add(phoneNumber);
        phoneBook.put(surname, list);
    }

    public ArrayList<String> get(String surname) {
        return phoneBook.get(surname);
    }
}
