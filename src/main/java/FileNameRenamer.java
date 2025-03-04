import java.io.File;

public class FileNameRenamer {
    public static void main(String[] args) {
        String folderPath = "E:\\CBZ formats IN PROGRESS\\MANGA\\Miss Koboyashi's Dragon Maid (15)";  // Replace with the actual folder path
        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String originalName = file.getName();

                String renamedString = rename(originalName);

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

    public static String cleanUpName(String originalName) {
        return originalName.replaceAll("\\s*\\(\\d{4}\\)*", "");
    }

    private static String rename(String originalName) {
        return originalName.replace("MKDM", "MK's Dragon Maid");
    }

    private static String changeZIPtoCBZtype(String originalName) {
        return originalName.contains(".zip")? originalName.replace(".zip", ".cbz") : originalName;
    }

    private static String reduceCBZFileNameBy(String originalName) {
        return originalName.substring(0, Math.min(originalName.length(), 19)) + ".cbz";
    }

    private static String reduceEPUBFileNameBy(String originalName) {
        return originalName.substring(0, Math.min(originalName.length(), 34)) + ".epub";
    }

    private static String removeUnkownAuthorFormat(String originalName) {
        return originalName.replace(" - Unknown", "");
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
