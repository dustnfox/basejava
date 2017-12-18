package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static ru.javawebinar.basejava.util.FileUtil.getFilesInDirectory;

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
        List<File> listOfFiles = getFilesInDirectory(dir);
        for(File f : listOfFiles) {
            System.out.println(f.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
