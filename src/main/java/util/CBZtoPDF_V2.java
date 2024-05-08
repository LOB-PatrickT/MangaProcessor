package util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CBZtoPDF_V2 {
    public static void main(String[] args) {
        // Specify the folder path containing the CBZ files
        String folderPath = "D:\\CBZ Formats\\Tokyo Revengers (31)";
        int imageReductionPercentage = 10;
        try {
            File folder = new File(folderPath);
            File[] cbzFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".cbz"));

            if (cbzFiles != null) {
                for (File cbzFile : cbzFiles) {
                    // Create a folder with the same name as the CBZ file (without extension)
                    String folderName = cbzFile.getName().replaceFirst("[.][^.]+$", "");
                    File destFolder = new File(folder, folderName);
                    destFolder.mkdir();

                    // Unzip the CBZ file to the destination folder
                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(cbzFile))) {
                        ZipEntry entry;
                        while ((entry = zis.getNextEntry()) != null) {
                            String entryName = entry.getName();
                            File entryFile = new File(destFolder, entryName);

                            // Create parent directories if necessary
                            new File(entryFile.getParent()).mkdirs();

                            // Write the entry to the destination file
                            try (OutputStream os = new FileOutputStream(entryFile)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = zis.read(buffer)) != -1) {
                                    os.write(buffer, 0, bytesRead);
                                }
                            }

//                            BufferedImage originalImage = ImageIO.read(entryFile);
//
//                            // Calculate the new dimensions based on the compression percentage
//                            int newWidth = (int) (originalImage.getWidth() * (1.0 - imageReductionPercentage / 100.0));
//                            int newHeight = (int) (originalImage.getHeight() * (1.0 - imageReductionPercentage / 100.0));
//
//                            // Create a new BufferedImage with the desired dimensions
//                            BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
//
//                            // Draw the original image onto the new image
//                            Graphics2D g2d = compressedImage.createGraphics();
//                            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
//                            g2d.dispose();
//
//                            // Save the compressed image to the specified output path
//                            File outputFile = new File(destFolder, entryName);
//                            ImageIO.write(compressedImage, "jpg", outputFile);
//                            System.out.println("writing reduced image to folder");
                        }
                        System.out.println("Unzipped: " + cbzFile.getName() + " to " + destFolder.getAbsolutePath());
                        JpgToPdfConverter.execute(destFolder.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No CBZ files found in the specified folder.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
