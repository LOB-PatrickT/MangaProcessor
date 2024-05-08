import java.io.File;

public class FileNameRenamer {
    public static void main(String[] args) {
        String folderPath = "D:\\OneDrive\\EBOOKS - ETSY and FB MARKETPLACE\\Fiction\\Anime & Manga\\Kaiju No_8 (11)\\PDF";  // Replace with the actual folder path
        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String originalName = file.getName();

                String renamedString = reduceFileName(originalName);
                if (!originalName.equals(renamedString)) {  // Check if renaming is necessary
                    File renamedFile = new File(folderPath + "/" + renamedString);
                    if (file.renameTo(renamedFile)) {
                        System.out.println(originalName + " renamed to " + renamedString);
                    } else {
                        System.out.println("Error renaming " + originalName);
                    }
                }
            }
        }
    }

    private static String reduceFileName(String originalName) {
        return originalName.replace(" - Naoya Matsumoto", "v");
    }

    private static String replaceFileTypeToCBZ(String originalName) {
        return originalName.contains(".zip")? originalName.replace(".zip", ".cbz") : originalName;
    }

    private static String reduceCBZFileNameBy(String originalName) {
        return originalName.substring(0, Math.min(originalName.length(), 16)) + ".cbz";
    }

    private static String removeAuthorFormat(String originalName) {
        return originalName.replace(" - Kohei Horikoshi", "");
    }

    private static String replaceAuthorFormat(String originalName) {
        return originalName.replace(" (Eiichiro Oda)", " - Eiichiro Oda");
    }

    private static String replaceVolFormat(String originalName) {
        return originalName.replace(", Vol. ", " Vol ");
    }

    private static String removeDigitalLucaz(String originalName) {
        return originalName.replace(" (Digital) (LuCaZ)", "");
    }

    private static String remove_modified(String originalName) {
        return originalName.replace("_modified", "");
    }

    private static String remove_A_Novel(String originalName) {
        return originalName.replace("_ A Novel", "");
    }
}
