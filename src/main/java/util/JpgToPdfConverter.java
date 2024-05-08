package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JpgToPdfConverter {
    public static void execute(String imagesFolderPath) {
        Path directoryPath = Paths.get(imagesFolderPath);
        String outputPDFPath = directoryPath.toString() + ".pdf";

        try (PDDocument document = new PDDocument()) {
            Files.list(directoryPath)
                    .filter(path -> path.toString().endsWith(".jpg") || path.toString().endsWith(".jpeg") || path.toString().endsWith(".png"))
                    .forEach(path -> {
                        try {
                            PDImageXObject pdImage = PDImageXObject.createFromFile(path.toString(), document);
                            PDRectangle pdRectangle = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
                            PDPage page = new PDPage(pdRectangle);
                            document.addPage(page);
                            System.out.println("adding page to PDF file");
                            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                                contentStream.drawImage(pdImage, 0, 0);
                            }
                        } catch (IOException e) {
                            System.err.println("Exception while trying to create image - " + e);
                        }
                    });

            document.save(outputPDFPath);
        } catch (IOException e) {
            System.err.println("Exception while trying to save PDF - " + e);
        }
    }

//    public static void main(String[] args) {
//        String volValue = "1";
//        Path directoryPath = Paths.get("D:\\CBZ Formats\\Neon Genesis Evangelion (5) - DONE\\Neon Genesis Evangelion 3-in-1 Edition v" + volValue);
//        String outputPDFPath = "D:\\CBZ Formats\\Neon Genesis Evangelion (5) - DONE\\Neon Genesis Evangelion 3-in-1 Edition v" + volValue + ".pdf";
//
//        try (PDDocument document = new PDDocument()) {
//            Files.list(directoryPath)
//                    .filter(path -> path.toString().endsWith(".jpg"))
//                    .forEach(path -> {
//                        try {
//                            PDImageXObject pdImage = PDImageXObject.createFromFile(path.toString(), document);
//                            PDRectangle pdRectangle = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
//                            PDPage page = new PDPage(pdRectangle);
//                            document.addPage(page);
//
//                            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                                contentStream.drawImage(pdImage, 0, 0);
//                            }
//                        } catch (IOException e) {
//                            System.err.println("Exception while trying to create image - " + e);
//                        }
//                    });
//
//            document.save(outputPDFPath);
//        } catch (IOException e) {
//            System.err.println("Exception while trying to save PDF - " + e);
//        }
//    }
}
