package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class JpgToPdfConverterV2 {

    public static void execute(String folderPath) {
        try {
            File folder = new File(folderPath);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png"));

            if (imageFiles != null && imageFiles.length > 0) {
                // Sort files by name to maintain order
                Arrays.sort(imageFiles);

                Document document = new Document();
                String outputPath = folderPath + ".pdf";
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

                // Adjust PDF compression settings
                writer.setFullCompression(); // No compression
                //writer.setCompressionLevel(PdfWriter.DEFAULT_COMPRESSION); // Default compression
                //writer.setCompressionLevel(PdfWriter.BEST_COMPRESSION); // Best compression

                document.open();
                for (File imageFile : imageFiles) {
                    Image image = Image.getInstance(imageFile.getAbsolutePath());
                    document.setPageSize(image);
                    document.newPage();
                    image.setAbsolutePosition(0, 0);
                    System.out.println("writing image..." + imageFile.getName());
                    document.add(image);
                }
                document.close();
                System.out.println("Created PDF: " + outputPath);
            } else {
                System.out.println("No image files found in the specified folder.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
