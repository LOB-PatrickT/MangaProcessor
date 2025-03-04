package util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;

public class HtmlScraper {

    public static void main(String[] args) {
        String filePath = "D:\\rawhtml.txt"; // Replace with the actual file path
        domScraper(filePath);
    }

    public static void domScraper(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource inputSource = new InputSource(new StringReader(htmlContent.toString().trim()));
            Document doc = builder.parse(inputSource);

            // Extract all images from the <div id="images-reader-container">
            NodeList imageNodes = doc.getElementById("images-reader-container").getElementsByTagName("img");
            for (int i = 0; i < imageNodes.getLength(); i++) {
                String src = imageNodes.item(i).getAttributes().getNamedItem("src").getTextContent();
                System.out.println("Image source: " + src);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}