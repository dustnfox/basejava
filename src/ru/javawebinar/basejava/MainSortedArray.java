package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

/**
 * Test for ru.javawebinar.basejava.storage.SortedArrayStorage class
 */
class MainSortedArray {
    private final static Storage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        System.out.println("SortedArrayStorage class test");
        System.out.println("----------------------------");
        MainTestStorage.testStorage(SORTED_ARRAY_STORAGE);
    }
}