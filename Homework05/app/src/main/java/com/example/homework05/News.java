package com.example.homework05;

public class News {
    public String author;
    public String title;
    public String url;
    public String urlToImage;
    public String publishedAt;

    @Override
    public String toString() {
        return "News{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }
}
