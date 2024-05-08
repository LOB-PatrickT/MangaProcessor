import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SplitPdf {
    public static void main(String[] args) throws IOException {
        // Load the input PDF file
        File inputFile = new File("C:\\Users\\Patrick\\Downloads\\Wotakoi _ Love Is Hard for Otaku, Vol 1.pdf");
        PDDocument document = PDDocument.load(inputFile);

        // Specify the page number to split
        int splitPageNumber = 136; // Change this to the desired page number

        // Create a Splitter instance
        Splitter splitter = new Splitter();

        // Split the PDF into multiple pages
        List<PDDocument> pages = splitter.split(document);

        // Save the specified page and the remaining pages
        int currentPageNumber = 1;
        Iterator<PDDocument> iterator = pages.iterator();
        while (iterator.hasNext()) {
            PDDocument page = iterator.next();
            if (currentPageNumber == splitPageNumber) {
                page.save("output_page_" + splitPageNumber + ".pdf");
            } else {
                page.save("output_remaining_pages.pdf");
            }
            currentPageNumber++;
            page.close();
        }

        System.out.println("PDF split successfully!");
        document.close();
    }
}