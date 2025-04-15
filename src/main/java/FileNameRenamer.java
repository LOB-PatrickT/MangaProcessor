import java.io.File;

public class FileNameRenamer {
    public static void main(String[] args) {
        String folderPath = "D:\\DOWNLOADS\\ungrouped CBZs";  // Replace with the actual folder path
        File folder = new File(folderPath);

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String originalName = file.getName();

                String renamedString = reduceCBZFileNameBy(originalName);

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

    private static String renameToKFX(String originalName) {
        return originalName.replace(".kepub", " KFX");
    }

    private static String rename(String originalName) {
        return originalName.replace(" .cbz", ".cbz");
    }

    private static String changeZIPtoCBZtype(String originalName) {
        return originalName.contains(".zip")? originalName.replace(".zip", ".cbz") : originalName;
    }

    private static String reduceCBZFileNameBy(String originalName) {
        return originalName.substring(0, Math.min(originalName.length(), 23)) + ".cbz";
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
