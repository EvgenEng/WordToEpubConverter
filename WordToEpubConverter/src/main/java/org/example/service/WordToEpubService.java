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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WordToEpubService {

    public void convert(String inputDir, String outputDir) {

        File out = new File(outputDir);
        if (!out.exists()) out.mkdirs();

        List<File> files = FileScanner.scanDocxFiles(inputDir);

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

            createEpub(epub, name, text.toString());

            System.out.println("Converted: " + file.getName());

        } catch (Exception e) {
            System.err.println("Error: " + file.getName());
            e.printStackTrace();
        }
    }

    private void createEpub(File file, String title, String content) throws IOException {

        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file))) {

            // mimetype (must be first)
            zip.putNextEntry(new ZipEntry("mimetype"));
            zip.write("application/epub+zip".getBytes());
            zip.closeEntry();

            add(zip, "META-INF/container.xml",
                    """
                    <?xml version="1.0"?>
                    <container version="1.0"
                     xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
                        <rootfiles>
                            <rootfile full-path="content.opf"
                             media-type="application/oebps-package+xml"/>
                        </rootfiles>
                    </container>
                    """);

            add(zip, "content.opf",
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <package xmlns="http://www.idpf.org/2007/opf" version="3.0">
                        <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
                            <dc:title>%s</dc:title>
                        </metadata>
                        <manifest>
                            <item id="html" href="text.html" media-type="application/xhtml+xml"/>
                        </manifest>
                        <spine>
                            <itemref idref="html"/>
                        </spine>
                    </package>
                    """.formatted(title));

            String html =
                    """
                    <html xmlns="http://www.w3.org/1999/xhtml">
                    <head><title>%s</title></head>
                    <body><pre>%s</pre></body>
                    </html>
                    """.formatted(title, escape(content));

            add(zip, "text.html", html);
        }
    }

    private void add(ZipOutputStream zip, String path, String data) throws IOException {
        zip.putNextEntry(new ZipEntry(path));
        zip.write(data.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private String escape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
