package util;

import java.io.File;

public class FolderCleaner {
    static boolean includePdfFileFlag = false;
    public static void deleteFolderContents(String folderPath, boolean includePdfFile) {
        includePdfFileFlag = includePdfFile;
        if (deleteContents(folderPath)) {
            System.out.println("All contents in the folder have been deleted successfully.");
        } else {
            System.out.println("Failed to delete contents from the folder.");
        }
    }

    public static boolean deleteContents(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Invalid folder path or folder does not exist.");
            return false;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively delete subdirectories
                    deleteContents(file.getAbsolutePath());
                } else if (!isPdfFile(file) || includePdfFileFlag) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete: " + file.getAbsolutePath());
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean isPdfFile(File file) {
        return file.getName().toLowerCase().endsWith(".pdf");
    }
}
