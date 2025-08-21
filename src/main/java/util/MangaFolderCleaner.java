package util;

import java.io.File;

public class MangaFolderCleaner {

    public static void main(String[] args) {
        String mainPath = "D:\\IMAGES_for_processing";

        File mainDir = new File(mainPath);
        if (mainDir.exists() && mainDir.isDirectory()) {
            for (File folder : mainDir.listFiles()) {
                if (folder.isDirectory()) {
                    File rawImagesFolder = new File(folder, "raw-images");
                    if (rawImagesFolder.exists() && rawImagesFolder.isDirectory()) {
                        File[] files = rawImagesFolder.listFiles();
                        if (files == null || files.length == 0) {
                            deleteFolder(folder);
                            System.out.println("Deleted folder: " + folder.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }
}