import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ImagesExtractorFromExcel {
    public static void main(String[] args) throws InterruptedException {
        String saveDirectory = "D:\\IMAGES_for_processing\\I Sold My Life For Ten Thousand Yen Per Year\\raw-images"; // Directory to save downloaded PNGs
        String excelFilePath = "D:\\manga-builder.xlsx"; // Path to the Excel file

        // Create the directory if it doesn't exist
        ImageExtractorUtil.createDirectoryIfNotExists(saveDirectory);

        try (Workbook workbook = new XSSFWorkbook(excelFilePath)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip the header row
                }

                Cell chapterCell = row.getCell(0);
                Cell htmlContentCell = row.getCell(1);

                if (chapterCell == null || htmlContentCell == null) {
                    continue; // Skip rows with empty cells
                }

                int chapter = 0;
                String subChapter = "";
                if(chapterCell.toString().contains(".") && Integer.valueOf(chapterCell.toString().split("\\.")[1]) > 0) {
                    chapter = Integer.valueOf(chapterCell.toString().split("\\.")[0]);
                    subChapter = "." + chapterCell.toString().split("\\.")[1];
                }
                else {
                    chapter = (int) chapterCell.getNumericCellValue();
                }
                String htmlContent = htmlContentCell.getStringCellValue();

                Document doc = Jsoup.parse(htmlContent);
                Elements imgTags = doc.select("img[src$=.jpg], img[src$=.png], img[src$=.jpeg], img[src*=webp], img[src$=.gif]"); // Select JPG and PNG files
                int page = 1;
                for (Element img : imgTags) {
                    String srcValue = img.attr("src");
                    System.out.println("saving image: " + srcValue + " for chapter " + chapter + " page " + page);
                    Thread.sleep(50);
                    ImageExtractorUtil.downloadPng(srcValue, saveDirectory, subChapter.contains(".")? chapter+1 : chapter, subChapter, page++);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
