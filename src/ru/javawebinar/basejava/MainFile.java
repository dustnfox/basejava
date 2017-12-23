package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * gkislin
 * 21.07.2016
 */
public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printDirectoryDeeply(dir);
    }

    public static void printDirectoryDeeply(File f) {
        printDirectoryDeeplyHelper(f, "");
    }


    private static void printDirectoryDeeplyHelper(File dir, String prefix) {
        final String PREFIX_ELEMENT = "  ";
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(prefix + "File: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println(prefix + "Directory: " + file.getName());
                    printDirectoryDeeplyHelper(file, prefix + PREFIX_ELEMENT);
                }
            }
        }
    }
}
