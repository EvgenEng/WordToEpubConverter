package org.example.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.util.FileScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConverterService {

    public void convert(String inputDir, String outputDir) {

        File out = new File(outputDir);
        if (!out.exists()) out.mkdirs();

        List<File> files = FileScanner.scanDocxFiles(inputDir);

        if (files.isEmpty()) {
            System.out.println("No DOCX files found");
            return;
        }

        for (File file : files) {
            convertFile(file, outputDir);
        }
    }

    private void convertFile(File file, String outputDir) {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument doc = new XWPFDocument(fis)) {

            StringBuilder text = new StringBuilder();

            for (XWPFParagraph p : doc.getParagraphs()) {
                text.append(p.getText()).append("\n");
            }

            String name = file.getName().replace(".docx", "");
            File epub = new File(outputDir, name + ".epub");

            writeSimpleEpub(epub, name, text.toString());

            System.out.println("Converted: " + file.getName());

        } catch (Exception e) {
            System.out.println("Error: " + file.getName());
            e.printStackTrace();
        }
    }

    private void writeSimpleEpub(File file, String title, String content) throws IOException {

        String html =
                "<html><head><title>" + title + "</title></head>" +
                        "<body><pre>" + escape(content) + "</pre></body></html>";

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(html.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String escape(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
