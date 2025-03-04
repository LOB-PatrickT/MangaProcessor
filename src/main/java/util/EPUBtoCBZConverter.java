package util;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class EPUBtoCBZConverter {

    public static void main(String[] args) {
        List<String> volumes = CollectFileNamesByType.execute("D:\\EPUB_for_processing", "epub");
//        List<String> volumes = Arrays.asList("Demon Slayer v01");
        for(String volumeName: volumes) {
            execute(volumeName);
        }
    }

    public static void execute(String volumeName) {
        String mainPath = "D:\\EPUB_for_processing";
        String fileName = volumeName;
        String tempImagesPath = "D:\\EPUB_for_processing\\temp images";
        String epubFilePath = "D:\\EPUB_for_processing\\" + fileName + ".epub"; // Replace with the actual path to your EPUB file
        String outputZipPath = "D:\\EPUB_for_processing\\" + fileName + ".zip"; // Specify the output ZIP file path

        try {
            // Open the EPUB file
            ZipFile epubFile = new ZipFile(epubFilePath);

            // Create a directory to store extracted images
            File outputDir = new File(tempImagesPath);
            outputDir.mkdirs();

            // Iterate through EPUB entries
            Enumeration<? extends ZipEntry> entries = epubFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().toLowerCase().endsWith(".jpg") || entry.getName().toLowerCase().endsWith(".png") || entry.getName().toLowerCase().endsWith(".jpeg")) {
                    String imageFileName = "";
                    // Extract image file
                    if(entry.getName().contains("index-1_1.jpg") || entry.getName().contains("index-1_1.png") || entry.getName().contains("index-1_1.jpg")) {
                        continue;
                    }
                    if(epubFile.stream().anyMatch(e -> e.getName().contains("cover.jpg")) && entry.getName().contains("kcc-0000-kcc.jpg")) {
                        continue;
                    }
                    System.out.println("image name: " + entry.getName());

                    if(entry.getName().contains("/")) {
                        imageFileName = entry.getName().split("/")[entry.getName().split("/").length - 1];
                    } else {
                        imageFileName = entry.getName();
                    }

                    InputStream inputStream = epubFile.getInputStream(entry);
                    File imageFile = new File(outputDir, imageFileName);
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();
                }
            }

            // Create a ZIP file containing the extracted images
            FileOutputStream zipOutputStream = new FileOutputStream(outputZipPath);
            ZipOutputStream zip = new ZipOutputStream(zipOutputStream);
            for (File image : outputDir.listFiles()) {
                ZipEntry zipEntry = new ZipEntry(image.getName());
                zip.putNextEntry(zipEntry);
                FileInputStream imageInputStream = new FileInputStream(image);
                byte[] imageBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = imageInputStream.read(imageBuffer)) != -1) {
                    zip.write(imageBuffer, 0, bytesRead);
                }
                imageInputStream.close();
                zip.closeEntry();
            }
            zip.close();
            zipOutputStream.close();

            System.out.println("Images extracted and zipped successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        ZIPtoCBZConverter.execute(mainPath);
        FolderCleaner.deleteFolderContents(tempImagesPath, false);
    }
}