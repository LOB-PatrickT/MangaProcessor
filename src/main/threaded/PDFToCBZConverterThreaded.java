package src.main.threaded;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import util.FolderCleaner;
import util.ZIPtoCBZConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    static String fileName = "Wind Breaker v02";
    static String mainPath = "D:\\PDF_for_processing\\";
    static String imagesOutputDir  = mainPath + "\\images\\";
    static int pagesProcessed = 0;
    static String pdfFilePath = mainPath + fileName + ".pdf"; // Replace with the actual PDF file path
    static String zipFileOutputPath = mainPath + fileName + ".zip"; // Replace with the desired ZIP output file path

    public static void main(String[] args) {
        int qualityReductionPercentage = 15; // Specify the percentage by which you want to reduce the image quality
        File pdfFile = new File(pdfFilePath);
        List<Thread> threadList = new ArrayList<>();
        try {
            PDDocument pdfDocument = PDDocument.load(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            int numPages = pdfDocument.getNumberOfPages();

            Path path = Paths.get(imagesOutputDir);
            if (!Files.exists(path) || Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                    System.out.println("Directory created: " + imagesOutputDir);
                    for (int i = 0; i < numPages; i++) {
                        BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300); // Set desired DPI

                        int finalI = i;
                        Thread thread = new Thread(() -> {
                            try {
                                // Save the image to a file
                                int newWidth = (int) (image.getWidth() * (1 - (qualityReductionPercentage / 100.0)));
                                int newHeight = (int) (image.getHeight() * (1 - (qualityReductionPercentage / 100.0)));
                                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                                resizedImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
                                String imageName = String.format("page_%03d.jpg", finalI + 1); // Adjust the filename format
                                File outputFile = new File(imagesOutputDir, imageName);
                                ImageIO.write(resizedImage, "jpg", outputFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        thread.setName("page: " + finalI);
                        System.out.println("processed " + thread.getName());
                        threadList.add(thread);
                        thread.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("PDF pages converted to separate image files.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean allThreadsCompleted = false;
        pagesProcessed = threadList.size();
        System.out.println("thread count: " + threadList.size());
        while (!allThreadsCompleted) {
            allThreadsCompleted = true; // Assume all threads are completed

            for (Thread thread : threadList) {
                if (thread.isAlive()) {
                    allThreadsCompleted = false; // At least one thread is still running
                    break;
                }
            }

            if (!allThreadsCompleted) {
                // Sleep for a short interval before checking again
                try {
                    Thread.sleep(1000); // Adjust as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        createZipFromImages();
        renameZipToCbz(mainPath);
    }

    public static void createZipFromImages() {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileOutputPath))) {
            File folder = new File(imagesOutputDir);
            File[] imageFiles = folder.listFiles();

            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    if (imageFile.isFile()) {
                        String imageName = imageFile.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(imageName));

                        // Read image content and write to ZIP
                        try (FileInputStream imageInputStream = new FileInputStream(imageFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = imageInputStream.read(buffer)) != -1) {
                                zipOutputStream.write(buffer, 0, bytesRead);
                            }
                        }
                        zipOutputStream.closeEntry();
                    }
                }
            }

            System.out.println("Images combined into ZIP file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renameZipToCbz(String mainPath) {
        ZIPtoCBZConverter.execute(mainPath);

        ZipFile cbzFile = null;
        try {
            cbzFile = new ZipFile(new File(mainPath + fileName + ".cbz"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numFiles = 0;

        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) cbzFile.entries();
        List<ZipEntry> entryList = Collections.list(entries);

        for (ZipEntry entry : entryList) {
            if (!entry.isDirectory()) {
                numFiles++;
            }
        }
        System.out.println("images in the PDF files: " + pagesProcessed);
        System.out.println("images in the CBZ files: " + numFiles);
        System.out.println("PDF to CBZ successful? " + (numFiles==pagesProcessed? "YES" : "NO"));
        if(numFiles==pagesProcessed || numFiles > 0) {
            System.out.println("cleaning up...");
            FolderCleaner.deleteFolderContents(imagesOutputDir, true);
        } else {
            System.out.println("something went wrong... JPG files retained");
        }
    }
}
