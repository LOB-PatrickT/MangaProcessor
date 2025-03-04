package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JpgToPdfConverterV3 {
    public static void execute(String folderPath, float compressionValue) {
        try {
            File folder = new File(folderPath);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png"));

            if (imageFiles != null && imageFiles.length > 0) {
                Arrays.sort(imageFiles);

                Document document = new Document();
                String outputPath = folderPath + ".pdf";
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

                document.open();
                for (File imageFile : imageFiles) {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    File compressedImageFile = compressImage(
                            bufferedImage,
                            imageFile.getParent(),
                            "compressed_" + imageFile.getName(),
                            compressionValue);

                    System.out.println("Writing image: " + compressedImageFile.getName());
                    Image image = Image.getInstance(compressedImageFile.getAbsolutePath());
                    image.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight());
                    document.newPage();
                    image.setAbsolutePosition(0, 0);
                    document.add(image);

                    compressedImageFile.delete(); // Clean up the temporary compressed image file
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

    public static File compressImage(BufferedImage image, String outputDir, String outputFileName, float compressionValue) throws IOException {
        File compressedImageFile = new File(outputDir, outputFileName);
        try (FileOutputStream fos = new FileOutputStream(compressedImageFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new IllegalStateException("No writers found");

            ImageWriter writer = writers.next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(compressionValue); // Adjust the compression quality (0.0 to 1.0, higher means better quality)
            }

            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
        }
        return compressedImageFile;
    }
}
