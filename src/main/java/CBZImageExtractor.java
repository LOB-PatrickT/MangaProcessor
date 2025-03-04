import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

public class CBZImageExtractor {
    public static void main(String[] args) {
        String cbzFolderPath = "D:\\CBZ_for_processing\\"; // Specify the folder containing CBZ files

        try {
            File cbzFolder = new File(cbzFolderPath);
            File[] cbzFiles = cbzFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".cbz"));

            if (cbzFiles != null) {
                for (File cbzFile : cbzFiles) {
                    extractImagesFromCBZ(cbzFile);
                }
            } else {
                System.err.println("No CBZ files found in the specified folder.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractImagesFromCBZ(File cbzFile) throws IOException {
        String outputFolderName = cbzFile.getName().replace(".cbz", "");
        String outputFolderPath = "D:\\CBZ_for_processing\\" + outputFolderName; // Specify the output folder path

        // Create the output folder if it doesn't exist
        Path outputPath = Paths.get(outputFolderPath);
        Files.createDirectories(outputPath);

        try (ZipFile zipFile = new ZipFile(cbzFile)) {
            zipFile.stream()
                    .filter(entry -> entry.getName().toLowerCase().endsWith(".jpg") || entry.getName().toLowerCase().endsWith(".png"))
                    .forEach(entry -> {
                        try {
                            String entryName = entry.getName();
                            String imageOutputPath = outputFolderPath + File.separator + entryName;

                            try (FileOutputStream fos = new FileOutputStream(imageOutputPath)) {
                                zipFile.getInputStream(entry).transferTo(fos);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            System.out.println("Extracted images from " + cbzFile.getName() + " to " + outputFolderPath);
        }
    }
}