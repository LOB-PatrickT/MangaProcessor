package util;

import java.io.File;

public class ZIPtoCBZConverter {

    public static void execute(String mainPath) {
        File folder = new File(mainPath);
        String cbzFileName = null;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if(file.isDirectory()) {
                    continue;
                }
                String originalName = file.getName();

                String renamedString = originalName.contains(".zip")? originalName.replace(".zip", ".cbz") : originalName;
                cbzFileName = renamedString;
                if (!originalName.equals(renamedString)) {  // Check if renaming is necessary
                    File renamedFile = new File(mainPath + "/" + renamedString);
                    if (file.renameTo(renamedFile)) {
                        System.out.println(originalName + " renamed to " + renamedString);
                    } else {
                        System.out.println("Error renaming " + originalName);
                    }
                }
            }
        }
    }

}
