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
    private static final int MAX_RETRIES = 5;
    private static final int BUFFER_SIZE = 4096;
    public static void main(String[] args) {
        String htmlContent = HtmlConstant.getHTMLContent();
        String saveDirectory = "D:\\IMAGES_for_processing\\Cardcaptor Sakura Clear Card\\raw-images"; // Directory to save downloaded PNGs

        // Create the directory if it doesn't exist
        createDirectoryIfNotExists(saveDirectory);

        try {
            Document doc = Jsoup.parse(htmlContent);
//            Elements imgTags = doc.select("img[src$=.jpg]"); // Select only JPG images
            Elements imgTags = doc.select("img[src$=.png]"); // Select only PNG images
//            Elements imgTags = doc.select("img[src*=webp]");
            for (Element img : imgTags) {
                String srcValue = img.attr("src");
                System.out.println("saving image: " + srcValue);
                downloadPng(srcValue, saveDirectory, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadPng(String pngUrl, String saveDirectory, boolean isSubChapter) throws IOException {
        int attempts = 0;
        boolean success = false;

        while (attempts < MAX_RETRIES && !success) {
            attempts++;
            try {
                URL url = new URL(pngUrl);
//                System.out.println(extractChapterNumber(pngUrl));
                String rawFileName = extractChapterNumber(pngUrl);
                String finalFileName = null;
                if(isSubChapter) {
                    int fileChapterName = Integer.parseInt(rawFileName.split("\\.")[0]);
                    String subFileChapterNamePlus = null;
                    if(fileChapterName < 99 & fileChapterName > 9) {
                        if(fileChapterName + 1 > 99) {
                            subFileChapterNamePlus = "0" + ++fileChapterName;
                        }
                        else {
                            subFileChapterNamePlus = "00" + ++fileChapterName;
                        }
                    } else {
                        subFileChapterNamePlus = "000" + ++fileChapterName;
                    }
                    finalFileName = /*pngUrl.substring(0, pngUrl.lastIndexOf('/')) + "/" + */subFileChapterNamePlus + "." + pngUrl.split("/")[5].split("\\.")[1] + ".png";
                } else {
                    finalFileName = rawFileName;
                }
                File outputFile = new File(saveDirectory, finalFileName);

                if (!outputFile.exists() || outputFile.length() == 0) {
                    try (InputStream in = url.openStream();
                         FileOutputStream out = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }

                    // Verify if file is correctly downloaded
                    if (outputFile.length() > 0) {
                        success = true;
                        System.out.println("Download successful: " + finalFileName);
                    } else {
                        System.out.println("Download failed (zero bytes), retrying... Attempt " + attempts);
                        outputFile.delete();
                    }
                } else {
                    success = true;
                    System.out.println("File already exists: " + finalFileName);
                }
            } catch (IOException e) {
                System.out.println("Download error, retrying... Attempt " + attempts);
            }
        }

        if (!success) {
            System.out.println("Failed to download file after " + MAX_RETRIES + " attempts.");
        }
    }

    public static String extractChapterNumber(String url) {
        String[] urlParts = url.split("/");
        return urlParts[5];
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

    public static boolean containsDotAndNumberAfter(String input) {
        return input.matches(".*\\.\\d+");
    }
}
