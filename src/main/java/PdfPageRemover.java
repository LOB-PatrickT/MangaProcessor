import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PdfPageRemover {
    public static void main(String[] args) {
        // Specify the folder path containing the PDF files
        String folderPath = "D:\\CBZ_for_processing\\output\\30percent";

        try {
            // Get a list of all files in the folder
            File folder = new File(folderPath);
            File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

            if (pdfFiles != null) {
                for (File pdfFile : pdfFiles) {
                    // Load the PDF document
                    PDDocument document = PDDocument.load(pdfFile);

                    // Check if the document has at least two pages
                    if (document.getNumberOfPages() >= 2) {
                        // Remove the second page (index 1)
                        document.removePage(1);

                        // Save the modified document
                        document.save(pdfFile);
                        document.close();

                        System.out.println("Second page removed from: " + pdfFile.getName());
                    } else {
                        System.out.println("Skipped: " + pdfFile.getName() + " (Less than 2 pages)");
                    }
                }
            } else {
                System.out.println("No PDF files found in the specified folder.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}