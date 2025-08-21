package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageChecker {

    public static void main(String[] args) {
        String imagePath = "D:\\IMAGES_for_processing\\Kuroko's Basketball\\raw-images\\0044-001.png"; // Replace with your image path
        boolean isValid = isImageFileCorrupted(imagePath);
        System.out.println("Is the image file corrupted? " + isValid);
    }

    public static boolean isImageFileCorrupted(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);
            return image == null; // Returns true if the image is successfully read
        } catch (IOException e) {
            return true; // Returns false if an exception occurs (e.g., file is corrupted)
        }
    }

}
