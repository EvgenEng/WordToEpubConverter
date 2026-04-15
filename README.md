# 📘 Word to EPUB Converter

A simple Java CLI tool that converts `.docx` Word documents into EPUB format using Apache POI.

---

## 🚀 Features

- Convert `.docx` files to EPUB
- Batch processing of directories
- Recursive folder scanning
- Lightweight (no external heavy dependencies)
- Generates valid EPUB (ZIP-based structure)

---

## 🛠 Tech Stack

- Java 17
- Maven
- Apache POI (for DOCX parsing)

---

## 📦 Build

```bash
mvn clean package

## ▶️ Run
java -jar target/WordToEpubConverter-1.0-SNAPSHOT.jar <inputDir> <outputDir>

Example:

java -jar target/WordToEpubConverter-1.0-SNAPSHOT.jar C:/docs C:/output

## 📂 Project Structure
org.example
     ├── App.java
     ├── service
     │     └── WordToEpubService.java
     ├── util
     │     └── FileScanner.java

## ⚠️ Notes
Only .docx supported
.doc not supported (Apache POI limitation)
EPUB is simplified but valid ZIP structure
