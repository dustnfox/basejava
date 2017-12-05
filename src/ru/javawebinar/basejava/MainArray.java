package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;

/**
 * Test for ru.javawebinar.basejava.storage.ArrayStorage class
 */
class MainArray {
    private final static Storage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) throws IOException {
        System.out.println("ArrayStorage class test");
        System.out.println("----------------------------");
        MainTestStorage.testStorage(ARRAY_STORAGE);

        ArrayStorage as = new ArrayStorage();
        as.save(new Resume("2", "name2"));
        as.save(new Resume("1", "name1"));
    }
}