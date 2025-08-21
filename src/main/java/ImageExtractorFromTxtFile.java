import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImageExtractorFromTxtFile {
    public static void main(String[] args) {
//        printOutChapterNums(76);
        extractImages();
    }

    public static void extractImages() {
        String saveDirectory = "D:\\IMAGES_for_processing\\The Grand Duke's Beloved Granddaughter\\raw-images"; // Directory to save downloaded PNGs
        String textFilePath = ImageExtractorFromTxtFile.class.getClassLoader().getResource("htmlmappings.txt").getPath(); // Path to the text file in resources // Path to the text file

        // Create the directory if it doesn't exist
        ImageExtractorUtil.createDirectoryIfNotExists(saveDirectory);

        try (BufferedReader br = new BufferedReader(new FileReader(textFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("cccc");

                int chapter = Integer.parseInt(parts[0].trim());
                String subChapter = "";
                String htmlContent = parts[1].trim();

                Document doc = Jsoup.parse(htmlContent);
                Elements imgTags = doc.select("img[src$=.jpg], img[src$=.png], img[src$=.jpeg], img[src*=webp]"); // Select JPG and PNG files
                int page = 1;
                for (Element img : imgTags) {
                    String srcValue = img.attr("src");
                    System.out.println("saving image: " + srcValue + " for chapter " + chapter + " page " + page);
                    Thread.sleep(100);
                    ImageExtractorUtil.downloadPng(srcValue, saveDirectory, subChapter.contains(".") ? chapter + 1 : chapter, subChapter, page++);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printOutChapterNums(int numOfChaps) {
        for (int i = 1; i < numOfChaps; i++) {
            System.out.println(i + "cccc");
        }
    }
}
