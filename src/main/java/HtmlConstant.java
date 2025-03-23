import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlConstant {
    public static String getHTMLContent() {
        String textFilePath = HtmlConstant.class.getClassLoader().getResource("rawhtml.txt").getPath(); // Path to the text file in resources // Path to the text file
        try {
            return Files.readString(Paths.get(textFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return an empty string or handle the exception as needed
        }
    }

    public static String chapterBreakerCount() {
        String filePath = "D:\\rawhtml.txt"; // Replace with the actual file path

        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return an empty string or handle the exception as needed
        }
    }
}
