package org.example;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BatchWordToEpubConverter {

    /**
     * Находит все Word файлы в указанной директории
     */
    public static List<String> findWordFiles(String directoryPath) {
        List<String> wordFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Директория не существует: " + directoryPath);
            return wordFiles;
        }

        File[] files = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".docx") || name.toLowerCase().endsWith(".doc"));

        if (files != null) {
            for (File file : files) {
                wordFiles.add(file.getAbsolutePath());
            }
        }

        return wordFiles;
    }

    /**
     * Конвертирует все Word файлы в директории
     */
    public static void convertAllWordFiles(String inputDir, String outputDir) {
        List<String> wordFiles = findWordFiles(inputDir);

        if (wordFiles.isEmpty()) {
            System.out.println("Word файлы не найдены в директории: " + inputDir);
            return;
        }

        // Создаем директорию для выходных файлов, если её нет
        File outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        for (String wordFile : wordFiles) {
            try {
                String fileName = new File(wordFile).getName();
                String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
                String epubFile = outputDir + File.separator + baseName + ".epub";

                Document document = new Document();
                document.loadFromFile(wordFile);
                document.saveToFile(epubFile, FileFormat.E_Pub);

                System.out.println("Конвертирован: " + fileName + " -> " + baseName + ".epub");

            } catch (Exception e) {
                System.err.println("Ошибка при конвертации файла " + wordFile + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Пример пакетной конвертации
        String inputDirectory = "C:/temp/word_files";
        String outputDirectory = "C:/temp/epub_files";

        convertAllWordFiles(inputDirectory, outputDirectory);
    }
}
