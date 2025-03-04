package util;

import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class CBZtoPDF_V2 {
    public static void main(String[] args) {
        String folderPath = "D:\\CBZ_for_processing";
        File folder = new File(folderPath);
        File[] cbzFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".cbz"));

        if (cbzFiles != null && cbzFiles.length > 0) {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            Arrays.stream(cbzFiles).forEach(cbzFile -> executor.submit(() -> processCBZFile(cbzFile, folder)));
            executor.shutdown();
        } else {
            System.out.println("No CBZ files found in the specified folder.");
        }
    }

    private static void processCBZFile(File cbzFile, File parentFolder) {
        String folderName = cbzFile.getName().replaceFirst("[.][^.]+$", "");
        File destFolder = new File(parentFolder, folderName);
        destFolder.mkdir();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(cbzFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[4096]; // Use a larger buffer for better performance
            while ((entry = zis.getNextEntry()) != null) {
                File entryFile = new File(destFolder, entry.getName());
                new File(entryFile.getParent()).mkdirs();

                try (OutputStream os = new FileOutputStream(entryFile)) {
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
            System.out.println("Unzipped: " + cbzFile.getName() + " to " + destFolder.getAbsolutePath());
            JpgToPdfConverterV3.execute(destFolder.getAbsolutePath(), 0.3f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}