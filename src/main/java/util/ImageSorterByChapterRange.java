package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImageSorterByChapterRange {
    public static void main(String args[]) {
        String mainPath = "D:\\IMAGES_for_processing\\One Piece (COLORED)\\";
        String rawImagesFolder = "raw-images";
        try {
            File folder = new File(mainPath + rawImagesFolder);
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
            List<int[]> volumePartitions = new LinkedList<>();
            // vol 1 to 20

            volumePartitions.add(new int[]{400, 409}); // Volume 42
            volumePartitions.add(new int[]{410, 419}); // Volume 43
            volumePartitions.add(new int[]{420, 430}); // Volume 44
            volumePartitions.add(new int[]{431, 440}); // Volume 45
            volumePartitions.add(new int[]{441, 449}); // Volume 46
            volumePartitions.add(new int[]{450, 459}); // Volume 47
            volumePartitions.add(new int[]{460, 470}); // Volume 48
            volumePartitions.add(new int[]{471, 481}); // Volume 49
            volumePartitions.add(new int[]{482, 491}); // Volume 50
            volumePartitions.add(new int[]{492, 502}); // Volume 51
            volumePartitions.add(new int[]{503, 512}); // Volume 52
            volumePartitions.add(new int[]{513, 522}); // Volume 53
            volumePartitions.add(new int[]{523, 532}); // Volume 54
            volumePartitions.add(new int[]{533, 541}); // Volume 55
            volumePartitions.add(new int[]{542, 551}); // Volume 56
            volumePartitions.add(new int[]{552, 562}); // Volume 57
            volumePartitions.add(new int[]{563, 573}); // Volume 58
            volumePartitions.add(new int[]{574, 584}); // Volume 59
            volumePartitions.add(new int[]{585, 594}); // Volume 60
            volumePartitions.add(new int[]{595, 603}); // Volume 61
            volumePartitions.add(new int[]{604, 614}); // Volume 62
            volumePartitions.add(new int[]{615, 626}); // Volume 63
            volumePartitions.add(new int[]{627, 636}); // Volume 64
            volumePartitions.add(new int[]{637, 646}); // Volume 65
            volumePartitions.add(new int[]{647, 656}); // Volume 66
            volumePartitions.add(new int[]{657, 667}); // Volume 67
            volumePartitions.add(new int[]{668, 678}); // Volume 68
            volumePartitions.add(new int[]{679, 690}); // Volume 69
            volumePartitions.add(new int[]{691, 700}); // Volume 70
            volumePartitions.add(new int[]{701, 711}); // Volume 71
            volumePartitions.add(new int[]{712, 721}); // Volume 72
            volumePartitions.add(new int[]{722, 731}); // Volume 73
            volumePartitions.add(new int[]{732, 742}); // Volume 74
            volumePartitions.add(new int[]{743, 752}); // Volume 75
            volumePartitions.add(new int[]{753, 763}); // Volume 76
            volumePartitions.add(new int[]{764, 775}); // Volume 77
            volumePartitions.add(new int[]{776, 785}); // Volume 78
            volumePartitions.add(new int[]{786, 795}); // Volume 79
            volumePartitions.add(new int[]{796, 806}); // Volume 80


            volumePartitions.add(new int[]{807, 816}); // Volume 81
            volumePartitions.add(new int[]{817, 827}); // Volume 82
            volumePartitions.add(new int[]{828, 838}); // Volume 83
            volumePartitions.add(new int[]{839, 848}); // Volume 84
            volumePartitions.add(new int[]{849, 858}); // Volume 85
            volumePartitions.add(new int[]{859, 869}); // Volume 86
            volumePartitions.add(new int[]{870, 879}); // Volume 87
            volumePartitions.add(new int[]{880, 889}); // Volume 88
            volumePartitions.add(new int[]{890, 900}); // Volume 89
            volumePartitions.add(new int[]{901, 910}); // Volume 90
            volumePartitions.add(new int[]{911, 921}); // Volume 91
            volumePartitions.add(new int[]{922, 931}); // Volume 92
            volumePartitions.add(new int[]{932, 942}); // Volume 93
            volumePartitions.add(new int[]{943, 953}); // Volume 94
            volumePartitions.add(new int[]{954, 964}); // Volume 95
            volumePartitions.add(new int[]{965, 974}); // Volume 96
            volumePartitions.add(new int[]{975, 984}); // Volume 97
            volumePartitions.add(new int[]{985, 994}); // Volume 98
            volumePartitions.add(new int[]{995, 1004}); // Volume 99
            volumePartitions.add(new int[]{1005, 1015}); // Volume 100
            volumePartitions.add(new int[]{1016, 1025}); // Volume 101
            volumePartitions.add(new int[]{1026, 1035}); // Volume 102
            volumePartitions.add(new int[]{1036, 1046}); // Volume 103
            volumePartitions.add(new int[]{1047, 1055}); // Volume 104

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
