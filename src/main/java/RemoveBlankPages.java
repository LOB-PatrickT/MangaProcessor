import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RemoveBlankPages {
    public static void main(String[] args) {
        try {
            // Load the PDF document
            PDDocument document = PDDocument.load(new File("C:\\Users\\Patrick\\Calibre Library\\Junji Ito\\Dissolving Classroom (2612)\\Dissolving Classroom - Junji Ito.pdf"));
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // Iterate through each page
            for (int page = 0; page < document.getNumberOfPages(); ) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                if (isBlank(bim)) {
                    // Remove the blank page
                    document.removePage(page);
                } else {
                    page++;
                }
            }

            // Save the updated document
            document.save("C:\\Users\\Patrick\\Calibre Library\\Junji Ito\\Dissolving Classroom (2612)\\Dissolving Classroom - Junji Ito_modified.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isBlank(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int threshold = (int) (width * height * 0.99); // 99% of the page area
        int whiteCount = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(img.getRGB(x, y));
                // Check if the color is light gray or white
                if (color.getRed() >= 248 && color.getGreen() == color.getRed() && color.getBlue() == color.getRed()) {
                    whiteCount++;
                }
            }
        }

        return whiteCount >= threshold;
    }
}