package org.example.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    public static List<File> scanDocxFiles(String dir) {

        List<File> result = new ArrayList<>();

        walk(new File(dir), result);

        return result;
    }

    private static void walk(File file, List<File> result) {

        if (!file.exists()) return;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;

            for (File f : files) {
                walk(f, result);
            }

        } else {

            String name = file.getName().toLowerCase();

            if (name.endsWith(".docx")) {
                result.add(file);
            }
        }
    }
}
