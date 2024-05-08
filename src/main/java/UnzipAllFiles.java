import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipAllFiles {
    public static void main(String[] args) {
        // Define the directory containing ZIP files
        Path zipDirectory = Paths.get("D:\\CBZ Formats\\Jujutsu Kaisen (21)");

        try (Stream<Path> files = Files.list(zipDirectory)) {
            files.forEach(file -> {
                // Check if the file is a ZIP file
                if (file.toString().endsWith(".zip")) {
                    // Create a directory with the same name as the ZIP file
                    String folderName = file.getFileName().toString().replace(".zip", "");
                    Path folderPath = zipDirectory.resolve(folderName);
                    try {
                        Files.createDirectories(folderPath);
                        // Unzip the ZIP file to the created directory
                        unzip(file.toFile(), folderPath.toFile());
                    } catch (IOException e) {
                        System.err.println("Error creating directory or unzipping file: " + file.getFileName());
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Error accessing the directory.");
            e.printStackTrace();
        }
    }

    private static void unzip(File zipFile, File outputPath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(outputPath, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // Fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // Write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            System.err.println("Error unzipping file " + zipFile.getName());
            e.printStackTrace();
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}