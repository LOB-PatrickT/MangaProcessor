import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CBZtoPDF_V1 {

    public static boolean isImageReduced = false;

    public static void main(String[] args) {
        String folderPath = "D:\\CBZ_for_processing"; // Replace with the actual folder path

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        convertCBZtoPDF(file.getAbsolutePath(), file.getName());
                    }
                }
            } else {
                System.err.println("Error reading files from the folder.");
            }
        } else {
            System.err.println("Invalid folder path or folder does not exist.");
        }
    }

    public static void convertCBZtoPDF(String cbzFileNameAbsolutePath, String outputPdfFileName) {
        String cbzFilePath = cbzFileNameAbsolutePath;
        String pdfFileName = outputPdfFileName.replace(".cbz", ".pdf");
        String pdfOutputPath = "D:\\cbz_for_processing\\output\\";
        int imageReducedByPercentage = 40;

        try {
            PDDocument pdfDocument = new PDDocument();

            // Read images from CBZ file (you'll need to implement this part)
            BufferedImage[] images = readImagesFromCBZ(cbzFilePath);

            for (BufferedImage image : images) {
                PDPage page = new PDPage();
                pdfDocument.addPage(page);
                System.out.println("processing image: " + image.toString());
                image = resizeImage(true, imageReducedByPercentage, image);
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, imageToBytes(image), "image");
                PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
                contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                contentStream.close();
            }

            if(isImageReduced) {
                String outputPath = pdfOutputPath + imageReducedByPercentage + "percent\\";
                File outputDir = new File(outputPath);

                // Create the directory if it doesn't exist
                if (!outputDir.exists()) {
                    boolean created = outputDir.mkdirs();
                    if (!created) {
                        System.err.println("Error creating output directory: " + outputPath);
                        // Handle the error appropriately (e.g., throw an exception)
                    }
                }

                pdfDocument.save(new File(outputPath, pdfFileName));
            }
            else {
                pdfDocument.save(new File(pdfOutputPath + pdfFileName));
            }
            pdfDocument.close();

            System.out.println("CBZ successfully converted to PDF!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resizeImage(boolean reduceImageToggle, int reduceByPercentage, BufferedImage bufferedImage) {
        BufferedImage resizedImage = null;
        System.out.println("width: " + bufferedImage.getWidth() + " --- height: " + bufferedImage.getHeight());
        if(reduceImageToggle) {
            int newWidth = (int) (bufferedImage.getWidth() * (1 - (reduceByPercentage / 100.0)));
            int newHeight = (int) (bufferedImage.getHeight() * (1 - (reduceByPercentage / 100.0)));
            resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            resizedImage.getGraphics().drawImage(bufferedImage.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
            System.out.println("image resized by " + reduceByPercentage + "%");
            isImageReduced = reduceImageToggle;
            return resizedImage;
        }
        return bufferedImage;
    }

    // Implement this method to read images from CBZ file
    private static BufferedImage[] readImagesFromCBZ(String cbzFilePath) {
        List<BufferedImage> images = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(cbzFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    System.out.println("processing entry: " + entry.getName());
                    images.add(ImageIO.read(zis));
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images.toArray(new BufferedImage[0]);
    }

    // Convert BufferedImage to byte array
    private static byte[] imageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos); // You can change "jpg" to "png" or other formats if needed
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }
}