package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CBZtoPDF_V3 {

    static float resizeFactor = 0.9f; // Adjust this value to control image resizing (1.0 = no resizing)
    static float compressionQuality = 0.9f; // Adjust this value to control image compression (1.0 = max quality, 0.0 = lowest quality)

    public static void convertCBZToPDF(String cbzFilePath, String outputPdfPath) {
        try (PDDocument pdfDocument = new PDDocument();
             ZipInputStream zis = new ZipInputStream(new FileInputStream(cbzFilePath))) {

            ZipEntry entry;
            int pageFileCount = 0;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    // Read and process each image
                    BufferedImage image = ImageIO.read(zis);
                    image = convertToARGB(image);
                    image = resizeImage(image, resizeFactor);
                    image = applyGammaCorrection(image, 1.0f);
                    // Compress the image
                    byte[] compressedImageBytes = compressImage(image, compressionQuality);

                    // Create a page with dimensions matching the image
                    PDRectangle pageSize = new PDRectangle(image.getWidth(), image.getHeight());
                    PDPage page = new PDPage(pageSize);
                    pdfDocument.addPage(page);

                    // Add the compressed image to the page
                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, compressedImageBytes, "image");
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

    private static BufferedImage convertToARGB(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(originalImage, 0, 0, null);
        return newImage;
    }

    private static BufferedImage applyGammaCorrection(BufferedImage image, double gamma) {
        BufferedImage correctedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Apply gamma correction
                red = (int) (255 * Math.pow((double) red / 255, gamma));
                green = (int) (255 * Math.pow((double) green / 255, gamma));
                blue = (int) (255 * Math.pow((double) blue / 255, gamma));

                int newRgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                correctedImage.setRGB(x, y, newRgb);
            }
        }
        return correctedImage;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, float resizeFactor) {
        int newWidth = (int) (originalImage.getWidth() * resizeFactor);
        int newHeight = (int) (originalImage.getHeight() * resizeFactor);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(originalImage.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
        return resizedImage;
    }

    private static byte[] compressImage(BufferedImage image, float quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality); // Set the compression quality
            }
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
        return baos.toByteArray();
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
                    convertCBZToPDF(cbzFile.getAbsolutePath(), outputPdfPath);
                });
                System.out.println("cbz count: " + ++cbzFileCount);
            }
            executor.shutdown();
        } else {
            System.out.println("No CBZ files found in the specified folder.");
        }
    }
}