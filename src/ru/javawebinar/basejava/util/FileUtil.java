package ru.javawebinar.basejava.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<File> getFilesInDirectory(File directory) {
        final ArrayList<File> list  = new ArrayList<>();
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for (File f : files) {
                    list.add(f);
                    if (f.isDirectory()) {
                        list.addAll(getFilesInDirectory(f));
                    }
                }
            }
        }
        return list;
    }
}
