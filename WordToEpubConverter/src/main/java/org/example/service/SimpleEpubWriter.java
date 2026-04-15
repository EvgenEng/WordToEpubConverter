package org.example.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class SimpleEpubWriter {

    public static void convert(File docxFile, String outputPath, String title) {

        try (FileInputStream fis = new FileInputStream(docxFile);
             XWPFDocument doc = new XWPFDocument(fis)) {

            StringBuilder text = new StringBuilder();

            for (XWPFParagraph p : doc.getParagraphs()) {
                text.append(p.getText()).append("\n");
            }

            String html =
                    "<html><head><title>" + title + "</title></head>" +
                            "<body><pre>" + escape(text.toString()) + "</pre></body></html>";

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(html.getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            throw new RuntimeException("Conversion failed: " + docxFile.getName(), e);
        }
    }

    private static String escape(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
