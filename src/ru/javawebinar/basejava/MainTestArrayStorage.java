package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ArrayStorage;
import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

/**
 * Test for com.javawebinar.basejava.storage.AbstractArrayStorage
 */
class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new ArrayStorage();
    private static final Storage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        System.out.println("----------------------------");
        System.out.println("ArrayStorage test.\n");
        testArrayStorage(ARRAY_STORAGE);
        System.out.println("----------------------------");
        System.out.println("SortedArrayStorage test.\n");
        testArrayStorage(SORTED_ARRAY_STORAGE);
        System.out.println("----------------------------");
    }

    private static void testArrayStorage(Storage storage) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");


        storage.save(r2);
        storage.save(r1);
        storage.save(r3);

        System.out.println("Get r1: " + storage.get(r1.getUuid()));
        System.out.println("Size: " + storage.size());

        System.out.println("Get dummy: " + storage.get("dummy"));

        printAll(storage);
        storage.delete(r2.getUuid());
        printAll(storage);
        storage.update(r3);
        printAll(storage);
        storage.clear();
        printAll(storage);

        System.out.println("Size: " + storage.size());        
    }


    private static void printAll(Storage storage) {
        System.out.println("\nGet All");
        for (Resume r : storage.getAll()) {
            System.out.println(r);
        }
    }
}
