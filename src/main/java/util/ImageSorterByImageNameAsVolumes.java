package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * this class will group images by volume if the images from weebcentral are already compiled by volumes instead of chapters
 */
public class ImageSorterByImageNameAsVolumes {
    public static void main(String args[]) {
        String mainPath = "D:\\IMAGES_for_processing\\Strobe Edge\\";
        String rawImagesFolder = "raw-images";
        try {
            File folder = new File(mainPath + rawImagesFolder);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

            if (imageFiles != null) {
                for (File imageSourceFile : imageFiles) {
                    System.out.println(imageSourceFile.getName());
                    String targetFolderPath = mainPath + "Vol " + (Integer.parseInt(imageSourceFile.getName().split("-")[0]) > 9 ? Integer.parseInt(imageSourceFile.getName().split("-")[0]) : "0" + Integer.parseInt(imageSourceFile.getName().split("-")[0]));
//                    File targetFolder = new File(imageSourceFile.getName().split(Pattern.quote("\\"))[4].split("-")[0]);
                    System.out.println(targetFolderPath);
//                    Files.copy(imageSourceFile.toPath(), new File(targetFolderPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    File targetFolder = new File(targetFolderPath);
                    if (!targetFolder.exists()) {
                        targetFolder.mkdirs(); // Create the target folder if it doesn't exist
                    }

                    FileInputStream inputStream = null;
                    FileOutputStream outputStream = null;

                    try {
                        inputStream = new FileInputStream(imageSourceFile);
                        File targetFile = new File(targetFolder, imageSourceFile.getName());
                        outputStream = new FileOutputStream(targetFile);

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        System.out.println("Image file copied successfully to " + targetFile.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("Error copying the image file: " + e.getMessage());
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            System.err.println("Error closing streams: " + e.getMessage());
                        }
                    }
                }
            }
            else {
                System.err.println("No image files found in the specified folder.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
