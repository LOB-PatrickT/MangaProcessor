import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import util.FolderCleaner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PNGtoJPGtoPDFConverter {
    // CHECK IF COVER EXISTS FIRST!!!
    public static void main(String[] args) {
        String volumeValue = "33";
        String mangaName = "Attack on Titan";
        String inputFolderPath = "D:\\IMAGES_for_processing\\" + mangaName + "\\Vol " + volumeValue; // Specify the folder containing PNG files
        String outputPdfPath = "D:\\IMAGES_for_processing\\" + mangaName + "\\" + mangaName + " Vol " + volumeValue + ".pdf";
        try {
            File inputFolder = new File(inputFolderPath);
            File[] pngFiles = inputFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (pngFiles != null) {
                for (File pngFile : pngFiles) {
                    convertPNGtoJPG(pngFile);
                }
            } else {
                System.err.println("No PNG files found in the specified folder.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path directoryPath = Paths.get(inputFolderPath);
        compileJPGtoPDF(directoryPath, outputPdfPath);
        FolderCleaner.deleteFolderContents(inputFolderPath, false);
    }

    private static void convertPNGtoJPG(File pngFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(pngFile);
        String jpgFileName = pngFile.getName().replace(".png", ".jpg");
        File jpgFile = new File(pngFile.getParent(), jpgFileName);

        // Convert PNG to JPG
        ImageIO.write(bufferedImage, "jpg", jpgFile);

        System.out.println("Converted " + pngFile.getName() + " to " + jpgFile.getName());
    }

    private static void compileJPGtoPDF(Path directoryPath, String pdfOutputPath) {
        try (PDDocument document = new PDDocument()) {
            Files.list(directoryPath)
                    .filter(path -> path.toString().endsWith(".jpg"))
                    .forEach(path -> {
                        try {
                            PDImageXObject pdImage = PDImageXObject.createFromFile(path.toString(), document);
                            PDRectangle pdRectangle = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
                            PDPage page = new PDPage(pdRectangle);
                            document.addPage(page);

                            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                                contentStream.drawImage(pdImage, 0, 0);
                            }
                        } catch (IOException e) {
                            System.err.println("Exception while trying to create image - " + e);
                        }
                    });

            document.save(pdfOutputPath);
        } catch (IOException e) {
            System.err.println("Exception while trying to save PDF - " + e);
        }
    }
}