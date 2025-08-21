package util;

import java.io.File;

public class FolderCreator {

    public static void main(String[] args) {
        String baseFolder = "D:\\IMAGES_for_processing\\Noragami - Stray God\\"; // Replace with your desired base folder path

        for (int i = 1; i <= 24; i++) {
            String folderName = "Vol " + (i < 10? "0" + i:i);
            String folderPath = baseFolder + File.separator + folderName;

            File folder = new File(folderPath);
            if (folder.mkdir()) {
                System.out.println("Created folder: " + folderName);
            } else {
                System.err.println("Failed to create folder: " + folderName);
            }
        }
    }
}