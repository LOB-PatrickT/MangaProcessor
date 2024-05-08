package util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

//import org.jsoup.nodes.Document;

public class WebScraper {

    public static void main(String[] args) {
        String url = "https://mangasee123.com/read-online/Wind-Breaker-NII-Satoru-chapter-27.html";
//        gargoyleScrape(url);
//        jSoupScrape(url);
//        seleniumScrape(url);
        domScraper(url);
    }


    public static void domScraper(String urlString) {
        try {
            URL url = new URL(urlString); // Replace with the URL you want to extract content from
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url.openStream());

            // Extract title
            String title = doc.getElementsByTagName("title").item(0).getTextContent();
            System.out.println("Title: " + title);

            // Extract other elements (e.g., links, divs, etc.) similarly

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seleniumScrape(String urlString) {
//        System.setProperty("webdriver.chrome.driver", "C:\\java_lib\\chromedriver.exe"); // Set path to chromedriver
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true); // Enable headless mode
//        WebDriver driver = new ChromeDriver(options);
//
//        try {
//            driver.get(urlString);
//            // Now you can interact with the page, extract data, etc.
//            System.out.println(driver.getPageSource()); // Print the page content
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit();
//        }
//        String url = urlString; // Specify the URL of the web page
//
//        try {
//            WebClient webClient = new WebClient(BrowserVersion.CHROME);
//            // Enable JavaScript in the WebClient
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            webClient.getOptions().setJavaScriptEnabled(false);
//
//            // Fetch the page
//            HtmlPage page = webClient.getPage(url);
//
//            // Wait for JavaScript to finish executing (adjust the timeout as needed)
//            webClient.waitForBackgroundJavaScript(3000);
//
//            // Now you can extract data from the page
//            System.out.println(page.asXml()); // Print the entire page content
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void gargoyleScrape(String urlString) {
//        try {
//            URL url = new URL(urlString);
//            WebClient webClient = new WebClient();
//            HtmlPage page = webClient.getPage(url);
//
//            // Example: Extract all <a> tags
//            for (Object obj : page.getAnchors()) {
//                if (obj instanceof HtmlAnchor) {
//                    HtmlAnchor anchor = (HtmlAnchor) obj;
//                    System.out.println("Link: " + anchor.getHrefAttribute());
//                    System.out.println("Text: " + anchor.asNormalizedText());
//                }
//            }
//
//            // Add more methods to extract other elements as needed
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void jSoupScrape(String url) {
//        try {
//            Document doc = Jsoup.connect(url).get();
//
//            // Example: Extract all <a> tags
//            Elements links = doc.select("img");
//            for (Element link : links) {
//                System.out.println("src: " + link.attr("src"));
//            }
//
//            // Add more selectors to extract other elements as needed
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
