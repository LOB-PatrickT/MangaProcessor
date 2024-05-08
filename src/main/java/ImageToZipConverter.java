import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ImageToZipConverter {
    static String volume = "14";
    static String mainPath = "D:\\IMAGES_for_processing\\Wind Breaker (14)\\Vol " + volume;
    static String zipFilePath = mainPath + "\\Wind Breaker v" + volume + ".zip"; // Replace with the desired ZIP output file path

    public static void main(String[] args) {

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File folder = new File(mainPath);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    ZipEntry zipEntry = new ZipEntry(imageFile.getName());
                    zipOut.putNextEntry(zipEntry);
                    System.out.println(imageFile.getName());
                    try (FileInputStream fileIn = new FileInputStream(imageFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fileIn.read(buffer)) != -1) {
                            zipOut.write(buffer, 0, bytesRead);
                        }
                    }
                    zipOut.closeEntry();
                }
                System.out.println("Images successfully zipped!");
            } else {
                System.err.println("No image files found in the specified folder.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}