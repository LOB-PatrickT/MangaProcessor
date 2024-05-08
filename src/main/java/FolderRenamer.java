import java.io.File;

public class FolderRenamer {
    public static void main(String[] args) {
        String targetFolderPath = "C:\\Users\\Patrick\\Downloads\\J. D. Robb"; // Specify the path to your folders

        File folder = new File(targetFolderPath);
        File[] subfolders = folder.listFiles(File::isDirectory);

        for (File subfolder : subfolders) {
            String originalName = subfolder.getName();
            String newName = originalName.replaceAll("\\(\\d+\\)", "").trim();

            File newFolder = new File(folder, newName);
            if (subfolder.renameTo(newFolder)) {
                System.out.println("Renamed: " + originalName + " -> " + newName);
            } else {
                System.err.println("Error renaming: " + originalName);
            }
        }
    }
}