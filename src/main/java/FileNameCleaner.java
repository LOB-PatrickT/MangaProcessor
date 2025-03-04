import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameCleaner {
    public static void main(String[] args) {
        String parentFolderPath = "C:\\Users\\Patrick\\OneDrive\\EBOOKS - ETSY and FB MARKETPLACE\\Fiction\\Anime & Manga\\Demon Slayer (23)";  // Replace with the actual folder path

        // Create a regular expression pattern to match the desired file name format
        Pattern pattern = Pattern.compile("^(.*?)(?:_Demon Slayer - Kimetsu no Yaiba - KCC)?\\.pdf$");

        // Recursively process folders up to the 2nd degree
        processFolders(new File(parentFolderPath), pattern);
    }

    private static void processFolders(File folder, Pattern pattern) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".pdf")) {
                        // Extract the base file name without extension
                        String fileName = file.getName();
                        Matcher matcher = pattern.matcher(fileName);
                        if (matcher.matches()) {
                            String newFileName = matcher.group(1) + ".pdf";

                            // Create a new File object with the updated file name
                            File newFile = new File(folder, newFileName);

                            // Rename the file
                            if (file.renameTo(newFile)) {
                                System.out.println("Renamed file: " + fileName + " -> " + newFileName);
                            } else {
                                System.out.println("Error renaming file: " + fileName);
                            }
                        }
                    } else if (file.isDirectory()) {
                        // Recurse into subfolders
                        processFolders(file, pattern);
                    }
                }
            }
        }
    }
}