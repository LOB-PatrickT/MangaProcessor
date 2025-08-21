package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class CBZtoPDF_V2 {

    static float widthScaleFactor = 1.1f; // Adjust this value to control width scaling
    static float heightScaleFactor = 1.0f;
    static float resizeFactor = 1.0f; // Adjust this value to control file size (1.0 = no resizing)

    public static void convertCBZToPDF(String cbzFilePath, String outputPdfPath, float resizeFactor) {
        try (PDDocument pdfDocument = new PDDocument();
             ZipInputStream zis = new ZipInputStream(new FileInputStream(cbzFilePath))) {

            ZipEntry entry;
            int pageFileCount = 0;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    // Read and process each image
                    BufferedImage image = ImageIO.read(zis);

                    // Create a page with dimensions matching the image
                    PDRectangle pageSize = new PDRectangle(1200 * resizeFactor, 1600 * resizeFactor);

                    PDPage page = new PDPage(pageSize);
                    pdfDocument.addPage(page);

                    // Add the image to the page
                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, imageToBytes(image), "image");
                    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                        contentStream.drawImage(pdImage, 0, 0, pageSize.getWidth(), pageSize.getHeight());
                    }
                    System.out.println("page file count: " + ++pageFileCount);
                }
                zis.closeEntry();
            }

            // Save the PDF
            pdfDocument.save(outputPdfPath);
            System.out.println("CBZ successfully converted to PDF: " + outputPdfPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos); // Save as JPG (or change to PNG if needed)
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        return imageBytes;
    }

    public static void main(String[] args) {
        String inputFolderPath = "D:\\CBZ_for_processing\\";
        String outputFolderPath = "D:\\CBZ_for_processing\\output\\";

        File inputFolder = new File(inputFolderPath);
        File[] cbzFiles = inputFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".cbz"));

        if (cbzFiles != null && cbzFiles.length > 0) {
            ExecutorService executor = Executors.newFixedThreadPool(4); // Limit to 4 threads
            int cbzFileCount = 0;
            for (File cbzFile : cbzFiles) {
                executor.submit(() -> {
                    String outputPdfPath = outputFolderPath + cbzFile.getName().replace(".cbz", ".pdf");
                    convertCBZToPDF(cbzFile.getAbsolutePath(), outputPdfPath, resizeFactor);
                });
                System.out.println("cbz count: " + ++cbzFileCount);
            }
            executor.shutdown();
        } else {
            System.out.println("No CBZ files found in the specified folder.");
        }
    }
}