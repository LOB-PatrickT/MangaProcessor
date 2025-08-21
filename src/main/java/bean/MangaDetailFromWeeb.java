package bean;

import lombok.Data;

import java.util.List;

@Data
public class MangaDetailFromWeeb {

//    private String mangaName;
//    private String mangaUrl;
    private String saveDirectory;
    private String baseUrl;
    private int startChapter;
    private int endChapter;
    private int startPageNumber;
    private List<Integer> subChaptersList;
    private boolean someChaptersStartsWithSubChapter;

    private MangaDetailFromWeeb(Builder builder) {
//        this.mangaName = builder.mangaName;
//        this.mangaUrl = builder.mangaUrl;
        this.saveDirectory = builder.saveDirectory;
        this.baseUrl = builder.baseUrl;
        this.startChapter = builder.startChapter;
        this.endChapter = builder.endChapter;
        this.startPageNumber = builder.startPageNumber;
        this.subChaptersList = builder.subChaptersList;
        this.someChaptersStartsWithSubChapter = builder.someChaptersStartsWithSubChapter;
    }

    public static class Builder {
//        private String mangaName;
//        private String mangaUrl;
        private String saveDirectory;
        private String baseUrl;
        private int startChapter;
        private int endChapter;
        private int startPageNumber;
        private List<Integer> subChaptersList;
        private boolean someChaptersStartsWithSubChapter;

//        public Builder mangaName(String mangaName) {
//            this.mangaName = mangaName;
//            return this;
//        }

//        public Builder mangaUrl(String mangaUrl) {
//            this.mangaUrl = mangaUrl;
//            return this;
//        }

        public Builder saveDirectory(String saveDirectory) {
            this.saveDirectory = saveDirectory;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder startChapter(int startChapter) {
            this.startChapter = startChapter;
            return this;
        }

        public Builder endChapter(int endChapter) {
            this.endChapter = endChapter;
            return this;
        }

        public Builder startPageNumber(int startPageNumber) {
            this.startPageNumber = startPageNumber;
            return this;
        }

        public Builder subChaptersList(List<Integer> subChaptersList) {
            this.subChaptersList = subChaptersList;
            return this;
        }

        public Builder someChaptersStartsWithSubChapter(boolean someChaptersStartsWithSubChapter) {
            this.someChaptersStartsWithSubChapter = someChaptersStartsWithSubChapter;
            return this;
        }

        public MangaDetailFromWeeb build() {
            return new MangaDetailFromWeeb(this);
        }
    }
}
