package util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlChecker {

    public static boolean isUrlNotFound(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD"); // Use HEAD request to avoid downloading the entire content
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_NOT_FOUND;
        } catch (IOException e) {
            // Handle exceptions (e.g., invalid URL, connection error)
            return false; // Assume not found if an exception occurs
        }
    }

    public static void main(String[] args) {
        String pngUrl = "https://official.lowee.us/manga/Wind-Breaker-NII-Satoru/0033-026.png"; // Replace with your PNG image URL
        if (isUrlNotFound(pngUrl)) {
            System.out.println("The PNG image was not found.");
        } else {
            System.out.println("The PNG image exists.");
        }
    }
}
