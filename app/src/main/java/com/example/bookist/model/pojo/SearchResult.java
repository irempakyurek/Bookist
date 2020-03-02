package com.example.bookist.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("items")
    private List<Book> listBooks;

    private String className;

    public List<Book> getListBooks() {
        return listBooks;
    }

    public void setListBooks(List<Book> listBooks) {
        this.listBooks = listBooks;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
