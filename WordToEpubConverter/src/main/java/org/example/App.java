package org.example;

import org.example.service.WordToEpubService;

public class App {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: <inputDir> <outputDir>");
            return;
        }

        new WordToEpubService().convert(args[0], args[1]);
    }
}