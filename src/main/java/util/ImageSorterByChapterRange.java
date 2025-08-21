package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImageSorterByChapterRange {
    public static void main(String args[]) {
        String mainPath = "D:\\IMAGES_for_processing\\07-Ghost\\";
        String rawImagesFolder = "raw-images";
        try {
            File folder = new File(mainPath + rawImagesFolder);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
            List<int[]> volumePartitions = new LinkedList<>();
            // vol 1 to 20

            volumePartitions.add(new int[]{1, 5}); // Volume 1
            volumePartitions.add(new int[]{6, 11}); // Volume 2
            volumePartitions.add(new int[]{12, 17}); // Volume 3
            volumePartitions.add(new int[]{18, 23}); // Volume 4
            volumePartitions.add(new int[]{24, 29}); // Volume 5
            volumePartitions.add(new int[]{30, 35});   // Volume 6
            volumePartitions.add(new int[]{36, 41});   // Volume 7
            volumePartitions.add(new int[]{42, 47});   // Volume 8
            volumePartitions.add(new int[]{48, 54});   // Volume 9
            volumePartitions.add(new int[]{55, 60});   // Volume 10
            volumePartitions.add(new int[]{61, 66});  // Volume 11
            volumePartitions.add(new int[]{67, 72}); // Volume 12
            volumePartitions.add(new int[]{73, 78}); // Volume 13
            volumePartitions.add(new int[]{79, 84}); // Volume 14
            volumePartitions.add(new int[]{85, 90}); // Volume 15
            volumePartitions.add(new int[]{91, 95}); // Volume 16
            volumePartitions.add(new int[]{96, 100}); // Volume 17
//            volumePartitions.add(new int[]{71, 74}); // Volume 18
//            volumePartitions.add(new int[]{75, 78}); // Volume 19
//            volumePartitions.add(new int[]{79, 82}); // Volume 20
//            volumePartitions.add(new int[]{83, 87}); // Volume 21
//            volumePartitions.add(new int[]{88, 91}); // Volume 22
//            volumePartitions.add(new int[]{92, 95}); // Volume 23
//            volumePartitions.add(new int[]{96, 99}); // Volume 24
//            volumePartitions.add(new int[]{100, 103}); // Volume 25
//            volumePartitions.add(new int[]{104, 107}); // Volume 26
//            volumePartitions.add(new int[]{108, 111}); // Volume 27
//            volumePartitions.add(new int[]{112, 115}); // Volume 28
//            volumePartitions.add(new int[]{116, 119}); // Volume 29
//            volumePartitions.add(new int[]{120, 123}); // Volume 30
//            volumePartitions.add(new int[]{124, 127}); // Volume 31
//            volumePartitions.add(new int[]{128, 131}); // Volume 32
//            volumePartitions.add(new int[]{132, 135}); // Volume 33
//            volumePartitions.add(new int[]{136, 139}); // Volume 34
//            volumePartitions.add(new int[]{140, 143}); // Volume 35
//            volumePartitions.add(new int[]{144, 147}); // Volume 36
//            volumePartitions.add(new int[]{322, 331}); // Volume 37
//            volumePartitions.add(new int[]{332, 341}); // Volume 38
//            volumePartitions.add(new int[]{342, 351}); // Volume 39
//            volumePartitions.add(new int[]{352, 360}); // Volume 40
//            volumePartitions.add(new int[]{361, 371}); // Volume 41
//            volumePartitions.add(new int[]{372, 379}); // Volume 42
//            volumePartitions.add(new int[]{375, 384}); // Volume 43
//            volumePartitions.add(new int[]{385, 393}); // Volume 44
//            volumePartitions.add(new int[]{394, 402}); // Volume 45

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
