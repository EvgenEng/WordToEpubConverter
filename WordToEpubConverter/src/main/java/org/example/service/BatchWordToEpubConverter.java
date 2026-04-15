package org.example.service;

public class BatchWordToEpubConverter {

    public static void main(String[] args) {

        String input = "C:/temp/word_files";
        String output = "C:/temp/epub_files";

        new WordToEpubService().convert(input, output);
    }
}
