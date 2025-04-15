import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import util.FolderCleaner;
import util.ZIPtoCBZConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class PDFToCBZConverterThreaded {
    static String fileName = "The Quintessential Quintuplets v05";
    static String mainPath = "D:\\PDF_for_processing\\";
    static String imagesOutputDir  = mainPath + "\\images\\";
    static int pagesProcessed = 0;
    static String pdfFilePath = mainPath + fileName + ".pdf"; // Replace with the actual PDF file path
    static String zipFileOutputPath = mainPath + fileName + ".zip"; // Replace with the desired ZIP output file path

    public static void main(String[] args) {
        try {
            extractImagesFromPDF();
            createZipFromImages();
            renameZipToCbz();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void extractImagesFromPDF() throws IOException {
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            throw new FileNotFoundException("PDF file not found: " + pdfFilePath);
        }

        try (PDDocument pdfDocument = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            int numPages = pdfDocument.getNumberOfPages();

            Path outputDir = Paths.get(imagesOutputDir);
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            for (int i = 0; i < numPages; i++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300); // High DPI for better quality
                String imageName = String.format("page_%03d.jpg", i + 1);
                File outputFile = new File(imagesOutputDir, imageName);
                ImageIO.write(image, "jpg", outputFile);
                System.out.println("Extracted: " + imageName);
            }
        }
    }

    public static void createZipFromImages() throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileOutputPath))) {
            File folder = new File(imagesOutputDir);
            File[] imageFiles = folder.listFiles();

            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    if (imageFile.isFile()) {
                        zipOutputStream.putNextEntry(new ZipEntry(imageFile.getName()));

                        try (FileInputStream imageInputStream = new FileInputStream(imageFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                                zipOutputStream.write(buffer, 0, bytesRead);
                            }
                        }
                        zipOutputStream.closeEntry();
                        System.out.println("Added to ZIP: " + imageFile.getName());
                    }
                }
            }
        }
        System.out.println("ZIP file created: " + zipFileOutputPath);
    }

    public static void renameZipToCbz() {
        File zipFile = new File(zipFileOutputPath);
        File cbzFile = new File(zipFileOutputPath.replace(".zip", ".cbz"));

        if (zipFile.renameTo(cbzFile)) {
            System.out.println("Renamed to CBZ: " + cbzFile.getAbsolutePath());
        } else {
            System.out.println("Failed to rename ZIP to CBZ.");
        }
    }
}