import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageExtractorUtil {
    public static String subChapter = ".2";
    public static void main(String[] args) {
        String htmlContent = HtmlConstant.getHTMLContent();
        String saveDirectory = "D:\\IMAGES_for_processing\\Mieruko-chan\\raw-images"; // Directory to save downloaded PNGs
        int chapter = 50;

        // Create the directory if it doesn't exist
        createDirectoryIfNotExists(saveDirectory);

        try {
            Document doc = Jsoup.parse(htmlContent);
            Elements imgTags = doc.select("img[src$=.jpg], img[src$=.png], img[src$=.jpeg], img[src*=webp]"); // Select JPG and PNG files
//            Elements imgTags = doc.select("img[src*=webp]");
            int page = 1;
            for (Element img : imgTags) {
                String srcValue = img.attr("src");
                System.out.println("saving image: " + srcValue);
                downloadPng(srcValue, saveDirectory, chapter, subChapter, page++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadPng(String pngUrl, String saveDirectory, int chapter, String subChapter, int page) throws IOException {
//        Thread thread = new Thread(() -> {
        try {
            URL url = new URL(pngUrl);
            String fileName = "";
            if(chapter > 99) {
                fileName = "0" + chapter +  subChapter + "-" + page + ".png";
            }
            if(chapter > 9 && chapter < 100) {
                fileName = "00" + chapter + subChapter + "-" + page + ".png";
            }
            if(chapter < 10) {
                fileName = "000" + chapter + subChapter + "-" + page + ".png";
            }
//            fileName = chapter + "-" + page + ".png";
            File outputFile = new File(saveDirectory, fileName);
            if(!outputFile.exists()) {
                try (InputStream in = url.openStream();
                     FileOutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        });
//        thread.start();
    }

    public static void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directory created: " + directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
