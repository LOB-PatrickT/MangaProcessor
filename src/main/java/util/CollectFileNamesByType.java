package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectFileNamesByType {
    public static List<String> execute(String mainPath, String fileType) {
        String folderPath = mainPath;  // Replace with the actual folder path
        File folder = new File(folderPath);
        List<String> filesNames = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if(file.getName().contains("."+fileType)) {
                    String originalName = file.getName();
                    String strippedFileName = originalName.replace("." + fileType, "");
                    filesNames.add(strippedFileName);
                    System.out.println(strippedFileName);
                }
            }
        }
        return filesNames;
    }

    public static void main(String[] args) {
        execute("D:\\EPUB_for_processing", "epub");
    }
}
