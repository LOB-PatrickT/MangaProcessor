import java.io.File;

public class FileNameExtractor {
    public static void main(String[] args) {
        // Specify the folder path
        String folderPath = "D:\\temp_ebooks";

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String originalName = file.getName();
                    String modifiedName = originalName.replace(" - Danielle Steel", "").replace(".pdf", "");
                    System.out.println(modifiedName);
                }
            }
        } else {
            System.out.println("No files found in the specified folder.");
        }
    }
}