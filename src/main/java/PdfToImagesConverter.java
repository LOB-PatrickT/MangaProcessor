import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfToImagesConverter {
    static String newFilePath = "A Sign of Affection, Vol. 4 - Suu Morishita";
    static String imagesOutputDir  = "D:\\temp_ebooks\\" + newFilePath;
    static String pdfFilePath = "D:\\temp_ebooks\\" + newFilePath + ".pdf"; // Replace with the actual PDF file path
    static String zipFilePath = "D:\\temp_ebooks\\" + newFilePath + ".zip"; // Replace with the desired ZIP output file path

    public static void main(String[] args) {
        // Replace with the desired CBZ output file path
        int qualityReductionPercentage = 75; // Specify the percentage by which you want to reduce the image quality
        File pdfFile = new File(pdfFilePath);
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

//                        ImageIO.write(resizedImage, "jpg", outputFile);

                        int finalI = i;
                        Thread thread = new Thread(() -> {
                                try {
                                    BufferedImage image = pdfRenderer.renderImageWithDPI(finalI, 300); // Set desired DPI
                                    int newWidth = (int) (image.getWidth() * (1 - (qualityReductionPercentage / 100.0)));
                                    int newHeight = (int) (image.getHeight() * (1 - (qualityReductionPercentage / 100.0)));
                                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                                    resizedImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);

                                    // Save the image to a file
                                    String imageName = String.format("page_%03d.jpg", finalI + 1); // Adjust the filename format
                                    File outputFile = new File(imagesOutputDir, imageName);
                                    ImageIO.write(resizedImage, "jpg", outputFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        thread.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            pdfDocument.close();
            System.out.println("PDF pages converted to separate image files.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        createZipFromImages();
    }

    public static void createZipFromImages() {

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
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
}