import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebPImageDownloader {

    public static String saveDirectory = "D:\\IMAGES_for_processing\\Boukyaku Battery\\raw-images\\story";

    public static void main(String[] args) throws IOException {
        downloadJPGImages();
//        downloadWebPImages();
    }

    public static void downloadJPGImages() throws IOException {
        String jpgURL = "https://mangacentral.io/api/image/boukyaku-battery/vol-2-chapter-6-volume-2-chapter-6/";
        for (int i = 1; i <= 20; i++) {
            downloadPng(jpgURL + i + ".jpg", saveDirectory + "\\" + "Chapter 6 - Page " + i +  ".jpg");
        }
    }

    public static void downloadWebPImages() {
        String htmlContent = HtmlConstant.getHTMLContent();
        // Directory to save downloaded PNGs

        // Create the directory if it doesn't exist
        createDirectoryIfNotExists(saveDirectory);

        try {
            Document doc = Jsoup.parse(htmlContent);
            Elements imgTags = doc.select("img[src*=webp]");
            for (Element img : imgTags) {
                String srcValue = img.attr("src");
                downloadPng(srcValue, saveDirectory + "\\" + img.attr("alt").replace("Boukyaku Battery - ", "") + ".png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadPng(String pngUrl, String saveDirectory) throws IOException {
//        Thread thread = new Thread(() -> {
            try (BufferedInputStream in = new BufferedInputStream(new URL(pngUrl).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(saveDirectory)) {

                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }

                System.out.println("Image downloaded successfully to: " + saveDirectory);
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