package util;

import java.io.File;

public class WhiteImageDeleter {
    public static void main(String[] args) {
        String directoryPath = "D:\\IMAGES_for_processing\\Hajime No Ippo\\Vol 12"; // Replace with the actual directory path

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;

                        if (fileSizeInKB <= 22) {
                            if (file.delete()) {
                                System.out.println("Deleted file: " + file.getName());
                            } else {
                                System.err.println("Error deleting file: " + file.getName());
                            }
                        }
                    }
                }
            } else {
                System.err.println("Error listing files in the directory.");
            }
        } else {
            System.err.println("Invalid directory path or directory does not exist.");
        }
    }
}