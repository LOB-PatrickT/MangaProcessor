import bean.MangaDetailFromWeeb;
import util.UrlChecker;

import java.util.List;

public class ImageExtractorFromWeeb {

    public static MangaDetailFromWeeb getMangaDetailFromWeeb() {
        return new MangaDetailFromWeeb.Builder()
                .saveDirectory("D:\\IMAGES_for_processing\\Shadows House\\raw-images")
                .baseUrl("https://official.lowee.us/manga/Shadows-House/")
                .startPageNumber(1)
                .startChapter(1)
                .endChapter(222)
                .someChaptersStartsWithSubChapter(false)
                .subChaptersList(List.of())
                .build();
    }

    public static void main(String[] args) {
        // TODO: EDIT HERE FOR CHAPTER MODS
        boolean someChaptersStartsWithSubChapter = getMangaDetailFromWeeb().isSomeChaptersStartsWithSubChapter();
        List<Integer> subChaptersList = getMangaDetailFromWeeb().getSubChaptersList();
        // TODO: EDIT HERE FOR CHAPTER MODS

        int countOfImagesNotFound = 1;
        // Create the directory if it doesn't exist
        ImageExtractor.createDirectoryIfNotExists(getMangaDetailFromWeeb().getSaveDirectory());
        int chapter = getMangaDetailFromWeeb().getStartChapter(); //starting chapter
        int imagePageNumber = getMangaDetailFromWeeb().getStartPageNumber();
        String imageSource = null;
        String baseUrl = "https://official.lowee.us/manga/Kenrantaru-Grande-Scene/";
        try {
            outerWhile: while (chapter <= getMangaDetailFromWeeb().getEndChapter()) {
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
                innerWhile: while (!UrlChecker.isUrlNotFound(imageSource) || someChaptersStartsWithSubChapter) {
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
                                    ImageExtractor.downloadPng(imageSubSourceToTest, getMangaDetailFromWeeb().getSaveDirectory(), true);
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
//                        System.out.println("downloading chapter image: " + imageSource + " = counter before resetting: " + countOfImagesNotFound);
                        Thread.sleep(10);
                        ImageExtractor.downloadPng(imageSource, getMangaDetailFromWeeb().getSaveDirectory(), false);
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
