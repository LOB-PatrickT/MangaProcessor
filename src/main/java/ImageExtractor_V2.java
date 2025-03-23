import util.UrlChecker;

import java.util.List;

public class ImageExtractor_V2 {
    public static void main(String[] args) {
        String saveDirectory = "D:\\IMAGES_for_processing\\JJBA Colored\\raw-images"; // Directory to save downloaded PNGs

        // TODO: EDIT HERE FOR CHAPTER MODS
        boolean mainChapterStartsWithSubChapter = false;
        List<Integer> subChaptersList =
                List.of();
//        List.of(2, 4, 5, 7, 12, 17, 19, 21, 28, 30, 37, 38, 41, 43, 45, 51, 53, 59, 61, 67, 69, 75, 77);
        // TODO: EDIT HERE FOR CHAPTER MODS

        int countOfImagesNotFound = 0;
        // Create the directory if it doesn't exist
        ImageExtractor.createDirectoryIfNotExists(saveDirectory);
        int chapter = 29; //starting chapter
        int imagePageNumber = 1;
        String imageSource = null;
        String baseUrl = "https://scans.lastation.us/manga/Jojos-Bizarre-Adventure-Color/";
        try {
            outerWhile: while (chapter <= 131) {
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
                    if(imagePageNumber > 99) {
                        imageSource = baseUrl+"00" + chapter + "-" + imagePageNumber + ".png";
                    }
                    else if(imagePageNumber > 9) {
                        imageSource = baseUrl+"00" + chapter + "-0" + imagePageNumber + ".png";
                    } else {
                        imageSource = baseUrl+"00" + chapter + "-00" + imagePageNumber + ".png";
                    }
                }
                else {
                    if(imagePageNumber > 99) {
                        imageSource = baseUrl+"000" + chapter + "-" + imagePageNumber + ".png";
                    }
                    else if(imagePageNumber > 9) {
                        imageSource = baseUrl+"000" + chapter + "-0" + imagePageNumber + ".png";
                    } else {
                        imageSource = baseUrl+"000" + chapter + "-00" + imagePageNumber + ".png";
                    }
                }
                innerWhile: while (!UrlChecker.isUrlNotFound(imageSource) || mainChapterStartsWithSubChapter) {
                    if(imagePageNumber > 9) {
                        if(chapter > 999) {
                            if(imagePageNumber > 99) {
                                imageSource = baseUrl+"" + chapter + "-" + imagePageNumber + ".png";
                            } else {
                                imageSource = baseUrl+"" + chapter + "-0" + imagePageNumber + ".png";
                            }
                        } else if(chapter > 99) {
                            if(imagePageNumber > 99) {
                                imageSource = baseUrl+"0" + chapter + "-" + imagePageNumber + ".png";
                            } else {
                                imageSource = baseUrl+"0" + chapter + "-0" + imagePageNumber + ".png";
                            }
                        } else if(chapter > 9) {
                            if(imagePageNumber > 99) {
                                imageSource = baseUrl+"00" + chapter + "-" + imagePageNumber + ".png";
                            } else {
                                imageSource = baseUrl+"00" + chapter + "-0" + imagePageNumber + ".png";
                            }
                        } else {
                            // sample URL: https://official.lowee.us/manga/the-guy-she-was-interested-in-wasnt-a-guy-at-all/0001-100.png
                            if(imagePageNumber > 99) {
                                imageSource = baseUrl+"000" + chapter + "-" + imagePageNumber + ".png";
                            } else {
                                imageSource = baseUrl+"000" + chapter + "-0" + imagePageNumber + ".png";
                            }
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

                    // SET TO FALSE IF THERE ARE NO SUB-CHAPTERS //
//                    if(false) {
                    if(!subChaptersList.isEmpty() && subChaptersList.contains(chapter)) {
                        String imageSubChapterToCheck = imageSource.substring(0, imageSource.lastIndexOf('-'));
                        String imageSubPageNumberToAppend = imageSource.substring(imageSource.lastIndexOf('-') + 1);
                        for (int i = 1; i < 10; i++) {
                            int subImagePageNumber = Integer.parseInt(imageSubPageNumberToAppend.split("\\.")[0]);
                            // check if image source is existing or not found
                            inner:
                            for( ; subImagePageNumber < 100; subImagePageNumber++) {
                                String imageSubSourceToTest = "";
                                if(subImagePageNumber > 9) {
                                    imageSubSourceToTest = imageSubChapterToCheck  + "." + i + "-" + "0" + subImagePageNumber + ".png";
                                } else {
                                    imageSubSourceToTest = imageSubChapterToCheck  + "." + i + "-" + "00" + subImagePageNumber + ".png";
                                }
                                if(!UrlChecker.isUrlNotFound(imageSubSourceToTest)) {
                                    System.out.println("downloading sub-chapter image: " + imageSubSourceToTest + " = counter before resetting: " + countOfImagesNotFound);
                                    countOfImagesNotFound = 1;
                                    ImageExtractor.downloadPng(imageSubSourceToTest, saveDirectory, true);
                                }
                                else {
                                    countOfImagesNotFound++;
//                                    System.out.println("sub-chapter image not found: " + imageSubSourceToTest);
                                    break inner;
                                }
                            }
                        }
                    }

                    if(!UrlChecker.isUrlNotFound(imageSource)) {
                        countOfImagesNotFound = 1;
                        System.out.println("downloading chapter image: " + imageSource + " = counter before resetting: " + countOfImagesNotFound);
                        Thread.sleep(400);
                        ImageExtractor.downloadPng(imageSource, saveDirectory, false);
                    }
                    else {
                        countOfImagesNotFound++;
                        System.out.println("image not found: " + imageSource + " = counter: " + countOfImagesNotFound);
                    }
                    imagePageNumber++;
                    if(countOfImagesNotFound >= 30) {
                        break innerWhile;
                    }
                }
                chapter++;
                imagePageNumber = 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean containsDotAndNumberAfter(String input) {
        return input.matches(".*\\.\\d+");
    }
}
