import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class EpubEditor {

    public static void main(String[] args) throws IOException {
        EpubReader epubReader = new EpubReader();
//        getEPUBFilePaths("C:\\interesting ebooks to sell").forEach(word -> System.out.println(word));
        for(String epubAbsolutePathName: getEPUBFilePaths("C:\\Users\\Patrick\\Downloads\\One-Punch Man (27)")) {
            Book book = epubReader.readEpub(new FileInputStream(epubAbsolutePathName));
            String oldCoverImageName = book.getSpine().getResource(0).getHref().replace("Text/", "").replace(".xhtml", "") + ".jpg";
            String currentCoverPageContent = new String(book.getSpine().getResource(0).getData());
            String newCoverPageContent = currentCoverPageContent.replaceAll(oldCoverImageName, "cover.jpg");
            byte[] newCoverPageContentInBytes = newCoverPageContent.getBytes();
            book.getSpine().getResource(0).setData(newCoverPageContentInBytes);
            FileOutputStream outputStream = new FileOutputStream(epubAbsolutePathName);
            System.out.println("output: " + epubAbsolutePathName);
            EpubWriter epubWriter = new EpubWriter();
            epubWriter.write(book, outputStream);
            outputStream.close();
        }
    }

    public static List<String> getEPUBFilePaths(String folderPath) {
        try {
            return Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".epub"))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error scanning folder: " + e.getMessage());
            return List.of(); // Return an empty list in case of errors
        }
    }

}
