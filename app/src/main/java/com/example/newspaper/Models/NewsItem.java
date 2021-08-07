package com.example.newspaper.Models;

public class NewsItem {
    private String title, date;
    private String image;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String link;


    public NewsItem(String title, String date, String image,String link) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.link = link;
    }

    public NewsItem(String title, String image, String link) {
        this.title = title;
        this.image = image;
        this.link = link;
    }

    public NewsItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
