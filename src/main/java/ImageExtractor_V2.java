import util.UrlChecker;

public class ImageExtractor_V2 {
    public static void main(String[] args) {
        String saveDirectory = "D:\\IMAGES_for_processing\\Detective Conan (105)\\raw-images"; // Directory to save downloaded PNGs

        // Create the directory if it doesn't exist
        ImageExtractor.createDirectoryIfNotExists(saveDirectory);
        int chapter = 1;
        int imagePageNumber = 1;
        String imageSource = null;
        String baseUrl = "https://official.lowee.us/manga/Detective-Conan/";
//        String baseUrl = "https://scans.lastation.us/manga/Detective-Conan/";
        try {

            while (chapter < 1000) {
                if(chapter > 999) {
                    if(imagePageNumber > 9) {
                        imageSource = baseUrl+"" + chapter + "-0" + imagePageNumber + ".png";
                    } else {
                        imageSource = baseUrl+"" + chapter + "-00" + imagePageNumber + ".png";
                    }
                }
                else if(chapter > 99) {
                    if(imagePageNumber > 9) {
                        imageSource = baseUrl+"0" + chapter + "-0" + imagePageNumber + ".png";
                    } else {
                        imageSource = baseUrl+"0" + chapter + "-00" + imagePageNumber + ".png";
                    }
                }
                else if(chapter > 9) {
                    if(imagePageNumber > 9) {
                        imageSource = baseUrl+"00" + chapter + "-0" + imagePageNumber + ".png";
                    } else {
                        imageSource = baseUrl+"00" + chapter + "-00" + imagePageNumber + ".png";
                    }
                }
                else {
                    if(imagePageNumber > 9) {
                        imageSource = baseUrl+"000" + chapter + "-0" + imagePageNumber + ".png";
                    } else {
                        imageSource = baseUrl+"000" + chapter + "-00" + imagePageNumber + ".png";
                    }
                }
                while (!UrlChecker.isUrlNotFound(imageSource)) {
                    if(imagePageNumber > 9) {
                        if(chapter > 999) {
                            imageSource = baseUrl+"" + chapter + "-0" + imagePageNumber + ".png";
                        } else if(chapter > 99) {
                            imageSource = baseUrl+"0" + chapter + "-0" + imagePageNumber + ".png";
                        } else if(chapter > 9) {
                            imageSource = baseUrl+"00" + chapter + "-0" + imagePageNumber + ".png";
                        } else {
                            imageSource = baseUrl+"000" + chapter + "-0" + imagePageNumber + ".png";
                        }
                    } else {
                        if(chapter > 999) {
                            imageSource = baseUrl+"" + chapter + "-00" + imagePageNumber + ".png";
                        } else if(chapter > 99) {
                            imageSource = baseUrl+"0" + chapter + "-00" + imagePageNumber + ".png";
                        } else if(chapter > 9) {
                            imageSource = baseUrl+"00" + chapter + "-00" + imagePageNumber + ".png";
                        }  else {
                            imageSource = baseUrl+"000" + chapter + "-00" + imagePageNumber + ".png";
                        }
                    }
                    if(!UrlChecker.isUrlNotFound(imageSource)) {
                        System.out.println("downloading: " + imageSource);
                        ImageExtractor.downloadPng(imageSource, saveDirectory);
                    }
                    else {
                        System.out.println("image not found: " + imageSource);
                    }
                    imagePageNumber++;
                }
                chapter++;
                imagePageNumber = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
