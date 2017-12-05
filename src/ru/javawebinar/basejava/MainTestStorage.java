package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Test for ru.javawebinar.basejava.storage.Sorted interface
 */
class MainTestStorage {

    static void testStorage(Storage arrayStorage) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;
        try {
            while (true) {
                System.out.print("Введите одну из команд - (list | save uuid | delete uuid | get uuid | update uuid | clear | exit): ");
                String[] params = reader.readLine().trim().toLowerCase().split(" ");
                if (params.length < 1 || params.length > 2) {
                    System.out.println("Неверная команда.");
                    continue;
                }
                String uuid = null;
                if (params.length == 2) {
                    uuid = params[1].intern();
                }
                switch (params[0]) {
                    case "list":
                        printAll(arrayStorage);
                        break;
                    case "size":
                        System.out.println(arrayStorage.size());
                        break;
                    case "save":
                        r = new Resume(uuid);
                        arrayStorage.save(r);
                        printAll(arrayStorage);
                        break;
                    case "update":
                        r = new Resume(uuid);
                        arrayStorage.update(r);
                        printAll(arrayStorage);
                        break;
                    case "delete":
                        arrayStorage.delete(uuid);
                        printAll(arrayStorage);
                        break;
                    case "get":
                        System.out.println(arrayStorage.get(uuid));
                        break;
                    case "clear":
                        arrayStorage.clear();
                        printAll(arrayStorage);
                        break;
                    case "exit":
                        reader.close();
                        return;
                    default:
                        System.out.println("Неверная команда.");
                        break;
                }
            }
        }catch (IOException e) {
            System.out.println("Ошибка ввода/вывода.");
        }
    }

    private static void printAll(Storage arrayStorage) {
        List<Resume> all = arrayStorage.getAll();
        System.out.println("----------------------------");
        if (all.size() == 0) {
            System.out.println("Empty");
        } else {
            for (Resume r : all) {
                System.out.println(r);
            }
        }
        System.out.println("----------------------------");
    }
}

