package com.example.bookist.model.pojo;

public class MostReadBooks {

    private String Title;
    private String Pages ;
    private String Description ;
    private int Thumbnail ;

    public MostReadBooks() {
    }

    public MostReadBooks(String title, String pages, String description, int thumbnail) {
        Title = title;
        Pages = pages;
        Description = description;
        Thumbnail = thumbnail;
    }


    public String getTitle() {
        return Title;
    }

    public String getPages() {
        return Pages;
    }

    public String getDescription() {
        return Description;
    }

    public int getThumbnail() {
        return Thumbnail;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public void setPages(String pages) {
        Pages = pages;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}
