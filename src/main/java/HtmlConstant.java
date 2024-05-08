import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlConstant {
    public static String getHTMLContent() {
        String filePath = "D:\\rawhtml.txt"; // Replace with the actual file path

        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Return an empty string or handle the exception as needed
        }
    }
}
