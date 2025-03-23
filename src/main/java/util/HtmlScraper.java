package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlScraper {

    public static void main(String[] args) {
        String url = "https://comick.io/comic/sakamoto-days/5kwzB-volume-1-en"; // Replace with the actual file path
        scrapeImages(url);
    }

    public static void scrapeImages(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0")
                    .referrer("http://www.google.com")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Connection", "keep-alive")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "none")
                    .header("Sec-Fetch-User", "?1")
                    .get();
            // Extract all images from the <div id="images-reader-container">
            Element imagesContainer = doc.getElementById("images-reader-container");
            if (imagesContainer != null) {
                Elements imageElements = imagesContainer.getElementsByTag("img");
                for (Element img : imageElements) {
                    String src = img.attr("src");
                    System.out.println("Image source: " + src);
                }
            } else {
                System.out.println("No images found in the specified container.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}