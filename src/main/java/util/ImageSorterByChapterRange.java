package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImageSorterByChapterRange {
    public static void main(String args[]) {
        String mainPath = "D:\\IMAGES_for_processing\\Hunter x Hunter (COLORED)\\";
        String rawImagesFolder = "raw-images";
        try {
            File folder = new File(mainPath + rawImagesFolder);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
            List<int[]> volumePartitions = new LinkedList<>();
            // vol 1 to 20

            volumePartitions.add(new int[]{1, 8}); // Volume 1
            volumePartitions.add(new int[]{9, 17}); // Volume 2
            volumePartitions.add(new int[]{18, 26}); // Volume 3
            volumePartitions.add(new int[]{27, 35}); // Volume 4
            volumePartitions.add(new int[]{36, 44}); // Volume 5
            volumePartitions.add(new int[]{45, 54});   // Volume 6
            volumePartitions.add(new int[]{55, 63});   // Volume 7
            volumePartitions.add(new int[]{64, 73});   // Volume 8
            volumePartitions.add(new int[]{74, 83});   // Volume 9
            volumePartitions.add(new int[]{84, 93});   // Volume 10
            volumePartitions.add(new int[]{94, 103});  // Volume 11
            volumePartitions.add(new int[]{104, 115}); // Volume 12
            volumePartitions.add(new int[]{116, 127}); // Volume 13
            volumePartitions.add(new int[]{128, 139}); // Volume 14
            volumePartitions.add(new int[]{140, 151}); // Volume 15
            volumePartitions.add(new int[]{152, 163}); // Volume 16
            volumePartitions.add(new int[]{164, 175}); // Volume 17
            volumePartitions.add(new int[]{176, 187}); // Volume 18
            volumePartitions.add(new int[]{188, 199}); // Volume 19
            volumePartitions.add(new int[]{200, 211}); // Volume 20
            volumePartitions.add(new int[]{212, 223}); // Volume 21
            volumePartitions.add(new int[]{224, 235}); // Volume 22
            volumePartitions.add(new int[]{236, 247}); // Volume 23
            volumePartitions.add(new int[]{248, 260}); // Volume 24
            volumePartitions.add(new int[]{261, 270}); // Volume 25
            volumePartitions.add(new int[]{271, 280}); // Volume 26
            volumePartitions.add(new int[]{281, 290}); // Volume 27
            volumePartitions.add(new int[]{291, 300}); // Volume 28
            volumePartitions.add(new int[]{301, 310}); // Volume 29
            volumePartitions.add(new int[]{311, 320}); // Volume 30
            volumePartitions.add(new int[]{321, 330}); // Volume 31
            volumePartitions.add(new int[]{331, 340}); // Volume 32
            volumePartitions.add(new int[]{341, 350}); // Volume 33
            volumePartitions.add(new int[]{351, 360}); // Volume 34
            volumePartitions.add(new int[]{361, 370}); // Volume 35
            volumePartitions.add(new int[]{371, 380}); // Volume 36
            volumePartitions.add(new int[]{381, 390}); // Volume 37
//            volumePartitions.add(new int[]{381, 400}); // Volume 38

            int volCount = 1;
            if (imageFiles != null) {
                for(int[] partition: volumePartitions) {
                    String targetFolderPath = mainPath + "Vol " + (volCount > 9 ? volCount : "0" + volCount);
                    System.out.println(targetFolderPath);
                    File targetFolder = new File(targetFolderPath);
                    if (!targetFolder.exists()) {
                        targetFolder.mkdirs(); // Create the target folder if it doesn't exist
                    }
                    for (File imageSourceFile : imageFiles) {
                        FileInputStream inputStream = null;
                        FileOutputStream outputStream = null;
                        int currentChapterOfImage = convertStringsToIntegers(imageSourceFile.getName().split("-")[0]);
                        int openingChapter = partition[0];
                        int closingChapter = partition[1];
                        if(currentChapterOfImage >= openingChapter && currentChapterOfImage <= closingChapter) {
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
                    volCount++;
                }
            }
            else {
                System.err.println("No image files found in the specified folder.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer convertStringsToIntegers(String chapterNumber) {
        return Integer.parseInt(chapterNumber);
    }
}
