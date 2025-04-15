import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ImageToCBZConverter {
    static int volume = 0;
    static String targetPath = "D:\\IMAGES_for_processing\\I Got a Cheat Skill in Another World\\";
    public static void main(String[] args) {
        for(volume = 1; volume <= 5; volume++) {
            execute(volume);
        }
        changeZipToCbzRecursively(targetPath);
    }

    public static void execute(int volNumber) {
        String mainPath = targetPath + "Vol " + (volNumber < 10? "0" + volNumber : volNumber);
        String zipFilePath = mainPath + "\\" + mainPath.split(Pattern.quote("\\"))[2] + " v" + (volNumber < 10? "0" + volNumber : volNumber) + ".zip"; // Replace with the desired ZIP output file path
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File folder = new File(mainPath);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

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

    public static void changeZipToCbzRecursively(String folderPath) {
        File folder = new File(folderPath);
        findAndConvertZipFiles(folder);
    }

    private static void findAndConvertZipFiles(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        findAndConvertZipFiles(file);
                    } else if (file.getName().toLowerCase().endsWith(".zip")) {
                        String newFileName = file.getName().replaceFirst("\\.zip$", ".cbz");
                        File newFile = new File(file.getParent(), newFileName);
                        if (file.renameTo(newFile)) {
                            System.out.println("Renamed: " + file.getName() + " to " + newFile.getName());
                        } else {
                            System.out.println("Failed to rename: " + file.getName());
                        }
                    }
                }
            }
        }
    }
}