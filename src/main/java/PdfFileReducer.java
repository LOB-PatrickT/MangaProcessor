import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfFileReducer {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\Patrick\\Downloads\\Wotakoi\\Final copies\\Wotakoi _ Love Is Hard for Otaku, Vol 6.pdf"; // Specify the path to your PDF file
        int qualityReductionPercentage = 40; // Specify the percentage by which you want to reduce the image quality

        try {
            PDDocument document = PDDocument.load(new File(filePath));
            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300, ImageType.RGB); // Render image with 300 DPI
                // Reduce image quality
                // You can adjust the quality reduction algorithm based on your requirements
                // For example, using Java Advanced Imaging (JAI) to compress the image
                // Here, we're reducing the image quality by decreasing the image width and height
                int newWidth = (int) (image.getWidth() * (1 - (qualityReductionPercentage / 100.0)));
                int newHeight = (int) (image.getHeight() * (1 - (qualityReductionPercentage / 100.0)));
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                resizedImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);

                // Save the resized image
                File outputFile = new File("C:\\Users\\Patrick\\Downloads\\Wotakoi\\sample images\\" + (i + 1) + ".jpg");
                ImageIO.write(resizedImage, "jpg", outputFile);
            }

            document.close();
            System.out.println("Image quality reduction completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
