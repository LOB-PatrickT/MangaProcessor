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

public class ImageExtractor {
    public static void main(String[] args) {
        String htmlContent = HtmlConstant.getHTMLContent();
        String saveDirectory = "D:\\IMAGES_for_processing\\Wind Breaker (14)\\Vol 5\\CH 33"; // Directory to save downloaded PNGs

        // Create the directory if it doesn't exist
        createDirectoryIfNotExists(saveDirectory);

        try {
            Document doc = Jsoup.parse(htmlContent);
            Elements imgTags = doc.select("img[src$=.png]"); // Select only PNG images

            for (Element img : imgTags) {
                String srcValue = img.attr("src");
                downloadPng(srcValue, saveDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadPng(String pngUrl, String saveDirectory) throws IOException {
//        URL url = new URL(pngUrl);
//        String fileName = pngUrl.substring(pngUrl.lastIndexOf('/') + 1);
//        File outputFile = new File(saveDirectory, fileName);
//
//        try (InputStream in = url.openStream();
//             FileOutputStream out = new FileOutputStream(outputFile)) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = in.read(buffer)) != -1) {
//                out.write(buffer, 0, bytesRead);
//            }
//        }

        Thread thread = new Thread(() -> {
            try {
                URL url = new URL(pngUrl);
                String fileName = pngUrl.substring(pngUrl.lastIndexOf('/') + 1);
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
        });
//        thread.setName("page: " + pngUrl);
//        System.out.println("processed " + thread.getName());
        thread.start();
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
